package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;

public class IgnoreAddResponse implements BotResponse {
	
	private GenericMessageEvent<PircBotX> event;
	private String user;
	
	public IgnoreAddResponse(GenericMessageEvent<PircBotX> event, String user) {
		this.event = event;
		this.user = user;
	}

	@Override
	public void send(AsukaBot bot) {
		bot.getIgnoreManager().addIgnored(user);
		event.getUser().send().notice("User " + user + " added to ignore list");
	}

}
