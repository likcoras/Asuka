package io.github.likcoras.asuka.handler.response;

import io.github.likcoras.asuka.AsukaBot;

public class EmptyResponse implements BotResponse {

	private static final EmptyResponse INSTANCE = new EmptyResponse();

	private EmptyResponse() {
	}

	public static EmptyResponse get() {
		return INSTANCE;
	}

	@Override
	public void send(AsukaBot bot) {
	} // Do absolutely nothing

}
