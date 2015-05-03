package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.DataHandler;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class BotManager extends ListenerAdapter<PircBotX> {
	
	private static final Logger LOG = Logger.getLogger(BotManager.class);
	private static final Logger HANDLE = Logger.getLogger("Handler");
	
	private final PircBotX bot;
	private final List<DataHandler> handlers;
	private final String password;
	
	public BotManager(final ConfigParser cfg) {
		
		bot = new PircBotX(configure(cfg));
		handlers = new ArrayList<DataHandler>();
		password = cfg.getProperty("adminpass");
		
	}
	
	public void registerHandler(final DataHandler handler) {
		
		synchronized (handlers) {
			
			handlers.add(handler);
			
		}
		
		LOG.info("Added handler: " + handler.getClass().getSimpleName());
		
	}
	
	public void start() throws IOException, IrcException {
		
		LOG.info("Starting IRC bot...");
		bot.startBot();
		
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> eve) {
		
		final String msg = eve.getMessage();
		
		for (final DataHandler handler : handlers)
			if (handler.isHandlerOf(msg))
				try {
					
					HANDLE.info("Handling query '" + msg + "' by "
						+ userIdentifier(eve.getUser()) + " with handler "
						+ handler.getClass().getSimpleName());
					
					eve.getChannel().send()
						.message(handler.getData(msg).ircString());
					
				} catch (final NoResultsException e) {
					
					if (e.getCause() != null) {
						
						HANDLE.error("Error while handing query: ",
							e.getCause());
						eve.getChannel()
							.send()
							.message(
								"Error("
									+ e.getCause().getClass().getSimpleName()
									+ "): " + e.getCause().getMessage());
						
					} else
						eve.getChannel().send().message("No results found");
					
				}
		
	}
	
	@Override
	public void onPrivateMessage(final PrivateMessageEvent<PircBotX> eve) {
		
		if (!eve.getMessage().startsWith(".quit"))
			return;
		
		if (!eve.getMessage().equals(".quit " + password)) {
			
			LOG.warn("Failed authentication attempt by "
				+ userIdentifier(eve.getUser()) + " (" + eve.getMessage() + ")");
			return;
			
		}
		
		LOG.info("Quit requested by " + userIdentifier(eve.getUser()));
		
		eve.respond("Understood, quitting.");
		bot.stopBotReconnect();
		bot.sendIRC().quitServer("Requested");
		
	}
	
	private Configuration<PircBotX> configure(final ConfigParser cfg) {
		
		final Builder<PircBotX> build = new Configuration.Builder<PircBotX>();
		
		build
			.addListener(new BotLogger())
			.addListener(this)
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
	
	private String userIdentifier(final User user) {
		
		return user.getNick() + "!" + user.getLogin() + "@"
			+ user.getHostmask();
		
	}
	
}
