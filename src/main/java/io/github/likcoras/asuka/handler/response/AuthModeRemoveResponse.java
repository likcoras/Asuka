package io.github.likcoras.asuka.handler.response;

import org.pircbotx.User;

import io.github.likcoras.asuka.AsukaBot;
import lombok.NonNull;

public class AuthModeRemoveResponse implements BotResponse {

	private User user;

	public AuthModeRemoveResponse(@NonNull User user) {
		this.user = user;
	}

	@Override
	public void send(AsukaBot bot) {
		bot.getAuthManager().removeLevel(user);
	}

}
