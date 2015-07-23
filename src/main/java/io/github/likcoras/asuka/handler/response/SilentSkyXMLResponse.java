package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.SilentSkyXMLHandler.SilentXMLData;
import lombok.NonNull;

public class SilentSkyXMLResponse implements BotResponse {

	private static final String HEAD_FORMAT = BotUtil.addFormat("&b%s&b chapter &b%d&b");
	private static final String MEDIAFIRE_FORMAT = BotUtil.addFormat("| &bMediafire:&b ");
	private static final String MEGA_FORMAT = BotUtil.addFormat("| &bMega:&b ");
	private static final String READER_FORMAT = BotUtil.addFormat("| &bReader:&b ");

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public SilentSkyXMLResponse(@NonNull GenericMessageEvent<PircBotX> event, @NonNull SilentXMLData data) {
		this.event = event;
		StringBuilder builder = new StringBuilder(String.format(HEAD_FORMAT, data.getProject(), data.getChapter()));
		if (!data.getMediafireLink().isEmpty())
			builder.append(MEDIAFIRE_FORMAT + data.getMediafireLink());
		if (!data.getMegaLink().isEmpty())
			builder.append(MEGA_FORMAT + data.getMegaLink());
		if (!data.getReaderLink().isEmpty())
			builder.append(READER_FORMAT + data.getReaderLink());
		message = builder.toString();
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else
			event.respond(message);
	}

}
