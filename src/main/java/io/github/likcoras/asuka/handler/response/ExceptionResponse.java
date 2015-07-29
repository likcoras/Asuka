package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ResponseException;
import io.github.likcoras.asuka.handler.Handler;

public class ExceptionResponse implements BotResponse {

	private Handler handler;
	private GenericMessageEvent<PircBotX> event;
	private Exception cause;
	private String target;

	public ExceptionResponse(Handler handler, GenericMessageEvent<PircBotX> event, Exception cause) {
		this.handler = handler;
		this.event = event;
		this.cause = cause;

		if (event instanceof MessageEvent)
			target = ((MessageEvent<PircBotX>) event).getChannel().getName();
		else
			target = event.getUser().getNick();
	}

	@Override
	public void send(AsukaBot bot) throws ResponseException {
		event.getBot().sendIRC().message(target, BotUtil.getExceptionMessage(handler, event, cause));
	}

}
