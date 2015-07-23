package io.github.likcoras.asuka;

import java.util.concurrent.Callable;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;

import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.Handler;
import io.github.likcoras.asuka.handler.response.BotResponse;

public class HandlerTask implements Callable<BotResponse> {

	private AsukaBot bot;
	private Handler handler;
	private Event<PircBotX> event;

	public HandlerTask(AsukaBot bot, Handler handler, Event<PircBotX> event) {
		this.bot = bot;
		this.handler = handler;
		this.event = event;
	}

	@Override
	public BotResponse call() throws HandlerException {
		return handler.handle(bot, event);
	}
}
