package io.github.likcoras.asuka.handler.response;

import java.time.Duration;
import java.time.Instant;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import lombok.NonNull;

public class UptimeResponse implements BotResponse {

	private static final String FORMAT = BotUtil.addFormat("I've been up for &b%s&b, since &b%s&b");

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public UptimeResponse(@NonNull GenericMessageEvent<PircBotX> event, Instant upsince) {
		this.event = event;
		Duration uptime = Duration.between(upsince, Instant.ofEpochMilli(event.getTimestamp()));
		String startMessage = BotUtil.formatTime(upsince);
		String uptimeMessage = String.format("%dh %02dm %02ds", uptime.toHours(), uptime.getSeconds() % 3600 / 60,
				uptime.getSeconds() % 60);
		message = String.format(FORMAT, uptimeMessage, startMessage);
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else
			event.getUser().send().message(message);
	}

}
