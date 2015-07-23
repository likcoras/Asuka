package io.github.likcoras.asuka;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.NickAlreadyInUseEvent;
import org.pircbotx.hooks.events.SocketConnectEvent;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BotLogger extends ListenerAdapter<PircBotX> {

	@Override
	public void onSocketConnect(SocketConnectEvent<PircBotX> event) {
		log.info("Connecting...");
	}

	@Override
	public void onConnect(ConnectEvent<PircBotX> event) {
		log.info("Connected!");
	}

	@Override
	public void onJoin(JoinEvent<PircBotX> event) {
		if (event.getUser().equals(event.getBot().getUserBot()))
			log.info("Joined " + event.getChannel().getName());
	}

	@Override
	public void onNickAlreadyInUse(NickAlreadyInUseEvent<PircBotX> event) {
		log.warn("Nick " + event.getUsedNick() + " already in use! Trying with " + event.getAutoNewNick());
	}

	@Override
	public void onKick(KickEvent<PircBotX> event) {
		if (event.getRecipient().equals(event.getBot().getUserBot()))
			log.warn("Kicked from " + event.getChannel().getName() + " by " + event.getUser().getNick() + " reason: "
					+ event.getReason());
	}

	@Override
	public void onDisconnect(DisconnectEvent<PircBotX> event) {
		log.info("Disconnected.");
	}

}
