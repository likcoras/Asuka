package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.UptimeResponse;

public class UptimeHandler implements Handler {
	
	private long upSince;
	
	@Override
	public void configure(BotConfig config) {}
	
	@Override
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event) throws HandlerException {
		if (event instanceof ConnectEvent)
			upSince = event.getTimestamp();
		else if (event instanceof GenericMessageEvent) {
			@SuppressWarnings("unchecked")
			GenericMessageEvent<PircBotX> messageEvent = (GenericMessageEvent<PircBotX>) event;
			if (BotUtil.isTrigger(messageEvent.getMessage(), "uptime"))
				return new UptimeResponse(messageEvent, upSince);
		}
		return new EmptyResponse();
	}
	
	@Override
	public void cleanUp() {}
	
}
