package io.github.likcoras.ssbot.bot;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.auth.AuthListener;
import io.github.likcoras.ssbot.backends.DataHandler;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;
import io.github.likcoras.ssbot.core.BotCoreHandlers;
import io.github.likcoras.ssbot.core.BotCoreResult;
import io.github.likcoras.ssbot.util.BotUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class BotManager extends ListenerAdapter<PircBotX> {
	
	private static final Logger LOG = Logger.getLogger(BotManager.class);
	private static final Logger HANDLE = Logger.getLogger("Handler");
	
	private final BotCoreHandlers coreHandlers;
	
	private final PircBotX bot;
	private final List<DataHandler> handlers;
	
	public BotManager(final ConfigParser cfg, final BotCoreHandlers coreHandlers) {
		
		this.coreHandlers = coreHandlers;
		
		handlers = new ArrayList<DataHandler>();
		bot = new PircBotX(configure(cfg));
		
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> eve) {
		
		final String msg = eve.getMessage();
		final User user = eve.getUser();
		final Channel chan = eve.getChannel();
		
		final BotCoreResult core = coreHandlers.handle(user, chan, msg);
		
		if (core.equals(BotCoreResult.QUIT)) {
			
			bot.stopBotReconnect();
			bot.sendIRC().quitServer();
			
		} else if (core.equals(BotCoreResult.HANDLE))
			doHandlers(user, chan, msg);
		
	}
	
	public void registerHandler(final DataHandler handler) {
		
		synchronized (handlers) {
			
			handlers.add(handler);
			
		}
		
	}
	
	public void start() throws IOException, IrcException {
		
		LOG.info("Starting IRC bot...");
		bot.startBot();
		
	}
	
	private Configuration<PircBotX> configure(final ConfigParser cfg) {
		
		final Builder<PircBotX> build = new Configuration.Builder<PircBotX>();
		
		build
			.addListener(new BotLogger())
			.addListener(new AuthListener(coreHandlers.getAuth()))
			.addListener(this)
			.setAutoNickChange(true)
			.setAutoReconnect(true)
			.setName(cfg.getProperty("ircnick"))
			.setLogin(cfg.getProperty("irclogin"))
			.setRealName(cfg.getProperty("ircrealname"))
			.setServer(cfg.getProperty("irchost"),
				Integer.parseInt(cfg.getProperty("ircport")));
		
		if (Boolean.parseBoolean(cfg.getProperty("ircssl")))
			build.setSocketFactory(new UtilSSLSocketFactory()
				.trustAllCertificates());
		
		if (!cfg.getProperty("ircnickserv").isEmpty())
			build.setNickservPassword(cfg.getProperty("ircnickserv"));
		
		if (!cfg.getProperty("ircpass").isEmpty())
			build.setNickservPassword(cfg.getProperty("ircpass"));
		
		final String[] chans = cfg.getProperty("ircchannels").split(",");
		for (final String chan : chans) {
			
			final String[] chanKey = chan.split(":");
			
			if (chanKey.length > 1)
				build.addAutoJoinChannel(chanKey[0], chanKey[1]);
			else
				build.addAutoJoinChannel(chan);
			
		}
		
		return build.buildConfiguration();
		
	}
	
	private void doHandlers(final User user, final Channel chan,
		final String msg) {
		
		for (final DataHandler handler : handlers)
			if (handler.isHandlerOf(msg))
				try {
					
					useHandler(user, chan, msg, handler);
					
				} catch (final NoResultsException e) {
					
					handleNoResult(chan, e);
					
				}
		
	}
	
	private void useHandler(final User user, final Channel chan,
		final String msg, final DataHandler handler) throws NoResultsException {
		
		HANDLE.info("Handling query '" + msg + "' by "
			+ BotUtils.userIdentifier(user) + " with handler "
			+ handler.getClass().getSimpleName());
		
		if (!coreHandlers.getAuth().checkAuth(handler, msg, user, chan)) {
			
			HANDLE
				.info("User did not have a high enough access level for executing the query '"
					+ msg + "'");
			chan.send().message(user, "Sorry, you're not allowed to do that!");
			return;
			
		}
		
		chan.send().message(handler.getData(msg).ircString());
		
	}
	
	private void handleNoResult(final Channel chan, final NoResultsException e) {
		
		if (e.getCause() != null) {
			
			HANDLE.error("Error while handing query: ", e.getCause());
			chan.send().message(
				"Error(" + e.getCause().getClass().getSimpleName() + "): "
					+ e.getCause().getMessage());
			
		} else
			chan.send().message("No results found");
		
	}
	
}
