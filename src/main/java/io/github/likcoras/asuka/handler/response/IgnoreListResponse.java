package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;

public class IgnoreListResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;
	
	public IgnoreListResponse(GenericMessageEvent<PircBotX> event) {
		this.event = event;
	}
	
	@Override
	public void send(AsukaBot bot) {
		StringBuilder list = new StringBuilder();
		for (String ignore : bot.getIgnoreManager().getIgnored())
			list.append(ignore + ", ");
		User user = event.getUser();
		if (list.length() < 1)
			user.send().notice("No ignored users");
		else
			user.send().notice("Ignored Users: " + list.substring(0, list.length() - 2));
	}

}
