package io.github.likcoras.asuka.handler.response;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.exception.ResponseException;

public interface BotResponse {

	public void send(AsukaBot bot) throws ResponseException;

}
