package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import lombok.NonNull;

public class QuitResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;
	private String reply;
	private String message;

	public QuitResponse(@NonNull GenericMessageEvent<PircBotX> event, @NonNull String reply, @NonNull String message) {
		this.event = event;
		this.reply = reply.replaceAll("%s", event.getUser().getNick());
		this.message = message.replaceAll("%s", event.getUser().getNick());
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(reply);
		else
			event.respond(reply);
		bot.getIrcBot().stopBotReconnect();
		bot.getIrcBot().sendIRC().quitServer(message);
	}

}
