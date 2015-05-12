package io.github.likcoras.ssbot.auth;

import java.util.HashMap;
import java.util.Map;

import org.pircbotx.UserLevel;

public class AuthChannelData {
	
	private Map<String, AuthUserData> users;
	
	AuthChannelData() {
		
		users = new HashMap<String, AuthUserData>();
		
	}
	
	boolean isEmpty() {
		
		return users.isEmpty();
		
	}
	
	void swapNick(String old, String user) {
		
		AuthUserData userData = users.get(old);
		
		if (userData != null)
			users.put(user, userData);
		
	}
	
	void setUser(String user, UserLevel level, boolean set) {
		
		AuthUserData userData = users.get(user);
		if (userData == null) {
			
			if (!set)
				return;
			
			userData = new AuthUserData();
			users.put(user, userData);
			
		}
		
		userData.setUserLevel(level, set);
		
		purgeUser(user);
		
	}
	
	private void purgeUser(String user) {
		
		if (users.get(user).isEmpty())
			users.remove(user);
		
	}
	
	void delUser(String user) {
		
		users.remove(user);
		
	}
	
	UserLevel getLevel(String user) {
		
		AuthUserData userData = users.get(user);
		
		if (userData == null)
			return null;
		
		return userData.getLevel();
		
	}
	
}
