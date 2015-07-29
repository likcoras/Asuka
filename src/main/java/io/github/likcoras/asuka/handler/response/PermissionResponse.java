package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;

public class PermissionResponse implements BotResponse {

	private static final String FORMAT = "You are not allowed to execute query ";

	private GenericMessageEvent<PircBotX> event;

	public PermissionResponse(GenericMessageEvent<PircBotX> event) {
		this.event = event;
	}

	@Override
	public void send(AsukaBot bot) {
		event.getUser().send().notice(FORMAT + event.getMessage());
	}

}
