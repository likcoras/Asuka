package io.github.likcoras.ssbot.auth;

import io.github.likcoras.ssbot.backends.AuthDataHandler;
import io.github.likcoras.ssbot.backends.DataHandler;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;

public class AuthHandler {
	
	private CustomUserPrefixHandler customPrefix;
	
	public AuthHandler() {
		
		customPrefix = new CustomUserPrefixHandler();
		
	}
	
	public boolean checkAuth(final UserLevel required, final User user,
		final Channel chan) {
		
		final UserLevel level = customPrefix.getLevel(chan, user);
		
		if (level != null && required.compareTo(level) <= 0)
			return true;
		
		return false;
		
	}
	
	public boolean checkAuth(final DataHandler handler, final String msg,
		final User user, final Channel chan) {
		
		if (!(handler instanceof AuthDataHandler))
			return true;
		
		final UserLevel required =
			((AuthDataHandler) handler).getAuthLevel(msg);
		
		if (required == null)
			return true;
		
		return checkAuth(required, user, chan);
		
	}
	
	CustomUserPrefixHandler getCustomPrefix() {
		
		return customPrefix;
		
	}
	
}
