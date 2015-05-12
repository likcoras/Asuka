package io.github.likcoras.ssbot.auth;

import java.util.TreeSet;

import org.pircbotx.UserLevel;

public class AuthUserData {
	
	private TreeSet<UserLevel> levels;
	
	AuthUserData() {
		
		levels = new TreeSet<UserLevel>();
		
	}
	
	UserLevel getLevel() {
		
		return levels.last();
		
	}
	
}
