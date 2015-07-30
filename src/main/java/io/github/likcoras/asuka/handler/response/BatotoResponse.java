package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;

public class BatotoResponse implements BotResponse {

	private static final String FORMAT = BotUtil
			.addFormat("&bName:&b %s | &bAuthor:&b %s | &bTags:&b %s | &bStatus:&b %s | &bLink:&b %s");

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public BatotoResponse(GenericMessageEvent<PircBotX> event, String title, String author, String genres,
			String status, String link) {
		this.event = event;
		message = String.format(FORMAT, title, author, genres, status, link);
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else
			event.respond(message);
	}

}
