package io.github.likcoras.ssbot.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;

public class CustomUserPrefixHandler {
	
	private final Map<Character, UserLevel> prefix;
	private final Map<String, AuthChannelData> data;
	
	CustomUserPrefixHandler() {
		
		prefix = new HashMap<Character, UserLevel>();
		prefix.put('~', UserLevel.OWNER);
		prefix.put('&', UserLevel.SUPEROP);
		prefix.put('@', UserLevel.OP);
		prefix.put('%', UserLevel.HALFOP);
		prefix.put('+', UserLevel.VOICE);
		
		data = new HashMap<String, AuthChannelData>();
		
	}
	
	void loadPrefix(final List<String> msg) {
		
		String prefixMsg = null;
		for (final String item : msg)
			if (item.startsWith("PREFIX")) {
				
				prefixMsg = item;
				break;
				
			}
		
		if (prefixMsg == null)
			return;
		
		setPrefix(prefixMsg);
		
	}
	
	void loadNames(List<String> msg) {
		// TODO parse NAMES
	}
	
	UserLevel getLevel(Channel chan, User user) {
		
		AuthChannelData chanData = data.get(chan.getName());
		
		if (chanData == null)
			return null;
		
		return chanData.getLevel(user);
		
	}
	
	private void setPrefix(final String prefixMsg) {
		
		final String modes =
			prefixMsg.substring(prefixMsg.indexOf("(") + 1,
				prefixMsg.indexOf(")"));
		final String prefixes = prefixMsg.substring(prefixMsg.indexOf(")") + 1);
		
		if (modes.length() != prefixes.length())
			throw new java.lang.AssertionError(
				"Length of prefix and mode must be equal", null);
		
		prefix.clear();
		for (int i = 0; i < modes.length(); i++) {
			
			final UserLevel level = levelForMode(modes.charAt(i));
			
			if (level != null)
				prefix.put(prefixes.charAt(i), level);
			
		}
		
	}
	
	private UserLevel levelForMode(final char mode) {
		
		if (mode == 'q')
			return UserLevel.OWNER;
		else if (mode == 'a')
			return UserLevel.SUPEROP;
		else if (mode == 'o')
			return UserLevel.OP;
		else if (mode == 'h')
			return UserLevel.HALFOP;
		else if (mode == 'v')
			return UserLevel.VOICE;
		
		return null;
		
	}
	
}
