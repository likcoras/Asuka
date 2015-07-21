package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;

public class QuitResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;
	private String reply;
	private String message;

	public QuitResponse(GenericMessageEvent<PircBotX> event, String reply, String message) {
		this.event = event;
		this.reply = reply.replaceAll("%s", event.getUser().getNick());
		this.message = message.replaceAll("%s", event.getUser().getNick());
	}

	@Override
	public void send(AsukaBot bot) {
		event.respond(reply);
		bot.getIrcBot().stopBotReconnect();
		bot.getIrcBot().sendIRC().quitServer(message);
	}

}
