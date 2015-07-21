package io.github.likcoras.asuka.handler.response;

import org.pircbotx.User;
import org.pircbotx.UserLevel;
import io.github.likcoras.asuka.AsukaBot;
import lombok.NonNull;

public class AuthModeResponse implements BotResponse {

	private User user;
	private UserLevel level;

	public AuthModeResponse(@NonNull User user, @NonNull UserLevel level) {
		this.user = user;
		this.level = level;
	}

	@Override
	public void send(AsukaBot bot) {
		bot.getAuthManager().setLevel(user, level);
	}

}
