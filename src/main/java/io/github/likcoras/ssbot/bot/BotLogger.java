package io.github.likcoras.ssbot.bot;

import org.apache.log4j.Logger;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.NickAlreadyInUseEvent;
import org.pircbotx.hooks.events.SocketConnectEvent;

public class BotLogger extends ListenerAdapter<PircBotX> {
	
	private static final Logger LOG = Logger.getLogger(BotLogger.class);
	
	@Override
	public void onSocketConnect(final SocketConnectEvent<PircBotX> event) {
		
		LOG.info("Connecting...");
		
	}
	
	@Override
	public void onConnect(final ConnectEvent<PircBotX> event) {
		
		LOG.info("Successfully connected to IRC.");
		
	}
	
	@Override
	public void onNickAlreadyInUse(final NickAlreadyInUseEvent<PircBotX> event) {
		
		LOG.warn("The nick " + event.getUsedNick()
			+ " is already in use! Trying with " + event.getAutoNewNick());
		
	}
	
	@Override
	public void onJoin(final JoinEvent<PircBotX> event) {
		
		if (event.getUser().equals(event.getBot().getUserBot()))
			LOG.info("Joined channel " + event.getChannel().getName());
		
	}
	
	@Override
	public void onKick(final KickEvent<PircBotX> event) {
		
		if (event.getRecipient().equals(event.getBot().getUserBot()))
			LOG.info("Kicked from " + event.getChannel().getName() + " for '"
				+ event.getReason() + "'");
		
	}
	
	@Override
	public void onDisconnect(final DisconnectEvent<PircBotX> event) {
		
		LOG.info("Disconnected from server.");
		
	}
	
}
