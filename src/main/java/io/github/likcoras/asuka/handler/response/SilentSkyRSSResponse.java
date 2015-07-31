package io.github.likcoras.asuka.handler.response;

import java.time.Instant;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;

public class SilentSkyRSSResponse implements BotResponse {

	private static final String FORMAT = BotUtil.addFormat("&bLatest:&b %s (%s) &b%s&b");

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public SilentSkyRSSResponse(GenericMessageEvent<PircBotX> event, String title, Instant date, String link) {
		this.event = event;
		message = String.format(FORMAT, title, BotUtil.formatTime(date), link);
	}

	@Override
	public void send(AsukaBot bot) {
		BotUtil.chanUserRespond(event, message);
	}

}
