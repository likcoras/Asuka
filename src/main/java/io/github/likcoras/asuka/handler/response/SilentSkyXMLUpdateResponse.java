package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;

public class SilentSkyXMLUpdateResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;

	public SilentSkyXMLUpdateResponse(GenericMessageEvent<PircBotX> event) {
		this.event = event;
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message("Updated xml");
		else
			event.respond("Updated xml");
	}

}
