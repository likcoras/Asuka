package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.SilentSkyXMLHandler.SilentXMLData;

public class SilentSkyXMLResponse implements BotResponse {
	
	private static final String FORMAT = BotUtil.addFormat("&b%s&b chapter &b%d&b | &bMediafire:&b %s | &bMega:&b %s | &bReader:&b %s");
	
	private GenericMessageEvent<PircBotX> event;
	private String message;
	
	public SilentSkyXMLResponse(GenericMessageEvent<PircBotX> event, SilentXMLData data) {
		this.event = event;
		message = String.format(FORMAT, data.getProject(), data.getChapter(), data.getMediafireLink(), data.getMegaLink(), data.getReaderLink());
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else 
			event.respond(message);
	}

}
