package io.github.likcoras.asuka.handler.response;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.exception.ResponseException;

public class RawResponse implements BotResponse {

	private String message;

	public RawResponse(String message) {
		this.message = message;
	}

	@Override
	public void send(AsukaBot bot) throws ResponseException {
		bot.getIrcBot().sendRaw().rawLine(message);
	}

}
