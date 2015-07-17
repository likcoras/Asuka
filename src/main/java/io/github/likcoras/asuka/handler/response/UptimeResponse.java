package io.github.likcoras.asuka.handler.response;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;

public class UptimeResponse implements BotResponse {
	
	private static final String FORMAT = "I've been up for &b%s&b, since &b%s&b";
	
	private GenericMessageEvent<PircBotX> event;
	private String message;
	
	public UptimeResponse(GenericMessageEvent<PircBotX> event, long upsince) {
		this.event = event;
		Instant since = Instant.ofEpochMilli(upsince);
		Duration uptime = Duration.ofMillis(event.getTimestamp() - upsince);
		String upsinceMessage = DateTimeFormatter.ofPattern("MMM dd hh:mm:ss 'UTC'", Locale.US).format(LocalDateTime.ofInstant(since, ZoneId.of("Z")));
		String uptimeMessage = String.format("%dh %dm %ds", uptime.getSeconds() / 3600, uptime.getSeconds() % 3600 / 60, uptime.getSeconds() % 60);
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
