package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;

public class SilentSkyRSSResponse implements BotResponse {

	private static final String FORMAT = BotUtil.addFormat("&bLatest:&b %s (%s) &b%s&b");

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public SilentSkyRSSResponse(GenericMessageEvent<PircBotX> event, String title, String date, String link) {
		this.event = event;
		message = String.format(FORMAT, title, date, link);
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else
			event.respond(message);
	}

}
