package io.github.likcoras.asuka.handler.response;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;

public class UptimeResponse implements BotResponse {

	private static final String FORMAT = BotUtil.addFormat("I've been up for &b%s&b, since &b%s&b");

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public UptimeResponse(GenericMessageEvent<PircBotX> event, long upsince) {
		this.event = event;
		long seconds = upsince / 1000L;
		long diff = event.getTimestamp() / 1000L - seconds;
		String upsinceMessage = DateTimeFormatter.ofPattern("MMM dd hh:mm:ss 'UTC'", Locale.US)
				.format(LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC));
		String uptimeMessage = String.format("%dh %dm %ds", diff / 3600, diff % 3600 / 60, diff % 60);
		message = String.format(FORMAT, uptimeMessage, upsinceMessage);
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else
			event.getUser().send().message(message);
	}

}
