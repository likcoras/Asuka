package io.github.likcoras.ssbot.auth;

import java.util.HashMap;
import java.util.Map;

import org.pircbotx.UserLevel;

public class AuthChannelData {
	
	private final Map<String, AuthUserData> users;
	
	AuthChannelData() {
		
		users = new HashMap<String, AuthUserData>();
		
	}
	
	boolean isEmpty() {
		
		return users.isEmpty();
		
	}
	
	void swapNick(final String old, final String user) {
		
		final AuthUserData userData = users.get(old);
		
		if (userData != null)
			users.put(user, userData);
		
	}
	
	void setUser(final String user, final UserLevel level, final boolean set) {
		
		AuthUserData userData = users.get(user);
		if (userData == null) {
			
			if (!set)
				return;
			
			userData = new AuthUserData();
			users.put(user, userData);
			
		}
		
		if (set)
			userData.setUserLevel(level);
		else
			userData.removeUserLevel(level);
		
		purgeUser(user);
		
	}
	
	private void purgeUser(final String user) {
		
		if (users.get(user).isEmpty())
			users.remove(user);
		
	}
	
	void delUser(final String user) {
		
		users.remove(user);
		
	}
	
	UserLevel getLevel(final String user) {
		
		final AuthUserData userData = users.get(user);
		
		if (userData == null)
			return null;
		
		return userData.getLevel();
		
	}
	
}
