package io.github.likcoras.ssbot.auth;

import java.util.TreeSet;

import org.pircbotx.UserLevel;

public class AuthUserData {
	
	private TreeSet<UserLevel> levels;
	
	AuthUserData() {
		
		levels = new TreeSet<UserLevel>();
		
	}
	
	boolean isEmpty() {
		
		return levels.isEmpty();
		
	}
	
	void setUserLevel(UserLevel level, boolean set) {
		
		if (set)
			levels.add(level);
		else
			levels.remove(level);
		
	}
	
	UserLevel getLevel() {
		
		return levels.last();
		
	}
	
}
