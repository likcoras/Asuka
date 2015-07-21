package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import lombok.NonNull;

public class NoResultResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;

	public NoResultResponse(@NonNull GenericMessageEvent<PircBotX> event) {
		this.event = event;
	}

	@Override
	public void send(AsukaBot bot) {
		event.respond("No results");
	}

}
