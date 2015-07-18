package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;

public class IgnoreDelResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;
	private String user;
	
	public IgnoreDelResponse(GenericMessageEvent<PircBotX> event, String user) {
		this.event = event;
		this.user = user;
	}

	@Override
	public void send(AsukaBot bot) {
		bot.getIgnoreManager().removeIgnored(user);
		event.getUser().send().notice("User " + user + " removed from ignore list");
	}

}
