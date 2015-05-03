package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.DataHandler;
import io.github.likcoras.ssbot.backends.NoResultsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class BotManager extends ListenerAdapter<PircBotX> {
	
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
		
	}
	
	public void start() throws IOException, IrcException {
		
		bot.startBot();
		
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> eve) {
		
		final String msg = eve.getMessage();
		
		for (final DataHandler handler : handlers)
			if (handler.isHandlerOf(msg))
				try {
					
					eve.getChannel().send()
						.message(handler.getData(msg).ircString());
					
				} catch (final NoResultsException e) {
					
					eve.getChannel()
						.send()
						.message(
							"[{}] No results found".replace("{}", handler
								.getClass().getName()));
					
				}
		
	}
	
	@Override
	public void onPrivateMessage(final PrivateMessageEvent<PircBotX> eve) {
		
		if (!eve.getMessage().equals(".quit " + password))
			eve.respond("Unknown command");
		
		eve.respond("Understood, quitting.");
		bot.stopBotReconnect();
		bot.sendIRC().quitServer("Requested by admin");
		
	}
	
	private Configuration<PircBotX> configure(final ConfigParser cfg) {
		
		final Builder<PircBotX> build = new Configuration.Builder<PircBotX>();
		
		build
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
	
}
