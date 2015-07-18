package io.github.likcoras.asuka.handler.response;

import org.pircbotx.User;
import org.pircbotx.UserLevel;
import io.github.likcoras.asuka.AsukaBot;

public class AuthModeResponse implements BotResponse {
	
	private User user;
	private UserLevel level;
	
	public AuthModeResponse(User user, UserLevel level) {
		this.user = user;
		this.level = level;
	}

	@Override
	public void send(AsukaBot bot) {
		bot.getAuthManager().setLevel(user, level);
	}
	
}
