package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;

public interface Handler {
	
	public void configure(BotConfig config) throws ConfigException;
	
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event) throws HandlerException;
	
	public void cleanUp() throws HandlerException;
	
}
