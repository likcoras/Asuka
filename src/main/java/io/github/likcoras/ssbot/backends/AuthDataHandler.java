package io.github.likcoras.ssbot.backends;

import org.pircbotx.UserLevel;

public interface AuthDataHandler extends DataHandler {
	
	public UserLevel getAuthLevel(String query);
	
}
