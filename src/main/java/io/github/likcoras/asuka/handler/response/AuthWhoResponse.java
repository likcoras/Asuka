package io.github.likcoras.asuka.handler.response;

import org.pircbotx.UserLevel;
import io.github.likcoras.asuka.AsukaBot;

public class AuthWhoResponse implements BotResponse {
	
	private String user;
	private UserLevel level;
	
	public AuthWhoResponse(String user, UserLevel level) {
		this.user = user;
		this.level = level;
	}
	
	@Override
	public void send(AsukaBot bot) {
		bot.getAuthManager().setLevel(user, level);
	}
	
}
