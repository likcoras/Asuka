package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;

public class SilentSkyXMLUpdateResponse implements BotResponse {

	private static final String MESSAGE = "Updated xml";
	private GenericMessageEvent<PircBotX> event;

	public SilentSkyXMLUpdateResponse(GenericMessageEvent<PircBotX> event) {
		this.event = event;
	}

	@Override
	public void send(AsukaBot bot) {
		BotUtil.chanUserRespond(event, MESSAGE);
	}

}
