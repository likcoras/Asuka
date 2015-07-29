package io.github.likcoras.asuka.out;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.exception.ResponseException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DefaultOutputManager implements OutputManager {

	private AsukaBot bot;

	public DefaultOutputManager(AsukaBot bot) {
		this.bot = bot;
	}

	@Override
	public void send(BotResponse response) {
		try {
			response.send(bot);
		} catch (ResponseException e) {
			log.error("Error while sending " + response.getClass().getSimpleName(), e);
		}
	}

}
