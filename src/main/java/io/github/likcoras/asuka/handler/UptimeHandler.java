package io.github.likcoras.asuka.handler;

import java.time.Instant;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.UptimeResponse;

public class UptimeHandler extends TranslatingHandler {

	private Instant upSince;

	@Override
	public BotResponse onConnect(AsukaBot bot, ConnectEvent<PircBotX> event) {
		upSince = Instant.ofEpochMilli(event.getTimestamp());
		return EmptyResponse.get();
	}

	@Override
	public BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event) {
		if (BotUtil.isTrigger(event.getMessage(), "uptime"))
			return new UptimeResponse(event, upSince);
		return EmptyResponse.get();
	}

	@Override
	public BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event) {
		if (BotUtil.isTrigger(event.getMessage(), "uptime"))
			return new UptimeResponse(event, upSince);
		return EmptyResponse.get();
	}

}
