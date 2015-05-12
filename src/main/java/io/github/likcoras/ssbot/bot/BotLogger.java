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
	public void onSocketConnect(final SocketConnectEvent<PircBotX> eve) {
		
		LOG.info("Connecting...");
		
	}
	
	@Override
	public void onConnect(final ConnectEvent<PircBotX> eve) {
		
		LOG.info("Successfully connected to IRC.");
		
	}
	
	@Override
	public void onNickAlreadyInUse(final NickAlreadyInUseEvent<PircBotX> eve) {
		
		LOG.warn("The nick " + eve.getUsedNick() + " is already in use!");
		
	}
	
	@Override
	public void onJoin(final JoinEvent<PircBotX> eve) {
		
		if (eve.getUser().equals(eve.getBot().getUserBot()))
			LOG.info("Joined channel " + eve.getChannel().getName());
		
	}
	
	@Override
	public void onKick(final KickEvent<PircBotX> eve) {
		
		if (eve.getRecipient().equals(eve.getBot().getUserBot()))
			LOG.info("Kicked from " + eve.getChannel().getName() + " for '"
				+ eve.getReason() + "'");
		
	}
	
	@Override
	public void onDisconnect(final DisconnectEvent<PircBotX> eve) {
		
		LOG.info("Disconnected from server.");
		
	}
	
}
