package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;

public class IgnoreHelpResponse implements BotResponse {
	
	private static final String[] FORMAT = {BotUtil.addFormat("Usage:"),
			BotUtil.addFormat("&badd <nick>:&b adds a nick"),
			BotUtil.addFormat("&bdel <nick>:&b deletes a nick"),
			BotUtil.addFormat("&blist:&b lists all ignores")};
	
	private GenericMessageEvent<PircBotX> event;
	
	public IgnoreHelpResponse(GenericMessageEvent<PircBotX> event) {
		this.event = event;
	}
	
	@Override
	public void send(AsukaBot bot) {
		for (String line : FORMAT)
			event.getUser().send().notice(line);
	}

}
