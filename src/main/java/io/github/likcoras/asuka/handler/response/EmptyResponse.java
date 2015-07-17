package io.github.likcoras.asuka.handler.response;

import io.github.likcoras.asuka.AsukaBot;

public class EmptyResponse implements BotResponse {
	
	@Override
	public void send(AsukaBot bot) {
		// Do absolutely nothing
	}
	
}
