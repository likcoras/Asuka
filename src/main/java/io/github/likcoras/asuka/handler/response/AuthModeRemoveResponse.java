package io.github.likcoras.asuka.handler.response;

import org.pircbotx.User;

import io.github.likcoras.asuka.AsukaBot;

public class AuthModeRemoveResponse implements BotResponse {

	private User user;
	
	public AuthModeRemoveResponse(User user) {
		this.user = user;
	}

	@Override
	public void send(AsukaBot bot) {
		bot.getAuthManager().removeLevel(user);
	}

}
