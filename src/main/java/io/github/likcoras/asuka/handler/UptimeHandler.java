package io.github.likcoras.asuka.handler;

import java.time.Instant;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.response.UptimeResponse;

public class UptimeHandler extends Handler {

	private Instant upSince;

	public UptimeHandler(AsukaBot bot) {
		super(bot);
	}

	@Override
	public void onConnect(ConnectEvent<PircBotX> event) {
		upSince = Instant.ofEpochMilli(event.getTimestamp());
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		if (BotUtil.isTrigger(event.getMessage(), "uptime"))
			getBot().send(new UptimeResponse(event, upSince));
	}

}
