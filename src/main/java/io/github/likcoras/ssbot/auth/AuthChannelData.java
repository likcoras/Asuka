package io.github.likcoras.ssbot.auth;

import java.util.HashMap;
import java.util.Map;

import org.pircbotx.User;
import org.pircbotx.UserLevel;

public class AuthChannelData {
	
	private Map<String, AuthUserData> users;
	
	AuthChannelData() {
		
		users = new HashMap<String, AuthUserData>();
		
	}
	
	UserLevel getLevel(User user) {
		
		AuthUserData userData = users.get(user.getNick());
		
		if (userData == null)
			return null;
		
		return userData.getLevel();
		
	}
	
}
