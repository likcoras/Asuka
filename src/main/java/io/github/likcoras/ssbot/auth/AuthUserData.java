package io.github.likcoras.ssbot.auth;

import java.util.TreeSet;

import org.pircbotx.UserLevel;

public class AuthUserData {
	
	private final TreeSet<UserLevel> levels;
	
	AuthUserData() {
		
		levels = new TreeSet<UserLevel>();
		
	}
	
	boolean isEmpty() {
		
		return levels.isEmpty();
		
	}
	
	void setUserLevel(final UserLevel level) {
		
		levels.add(level);
		
	}
	
	void removeUserLevel(final UserLevel level) {
		
		levels.remove(level);
		
	}
	
	UserLevel getLevel() {
		
		return levels.last();
		
	}
	
}
