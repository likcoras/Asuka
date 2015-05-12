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
	
	void setUserLevel(final UserLevel level, final boolean set) {
		
		if (set)
			levels.add(level);
		else
			levels.remove(level);
		
	}
	
	UserLevel getLevel() {
		
		return levels.last();
		
	}
	
}
