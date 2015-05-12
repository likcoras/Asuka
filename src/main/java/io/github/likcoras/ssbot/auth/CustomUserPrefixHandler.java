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
	
	void loadNames(List<String> msg) {
		
		AuthChannelData chanData = new AuthChannelData();
		for (String name : msg.get(3).split(" ")) {
			
			char prefixChar = name.charAt(0);
			
			if (prefix.containsKey(prefixChar))
				chanData.setUser(name.substring(1), prefix.get(prefixChar), true);
			
		}
		
		if (!chanData.isEmpty())
			data.put(msg.get(2), chanData);
		
	}
	
	void swapNick(String old, String user) {
		
		for (AuthChannelData chanData : data.values())
			chanData.swapNick(old, user);
		
	}
	
	void toggleLevel(Channel chan, User user, UserLevel level, boolean set) {
		
		AuthChannelData chanData = data.get(chan.getName());
		
		if (chanData == null) {
			
			if (!set)
				return;
			
			chanData = new AuthChannelData();
			data.put(chan.getName(), chanData);
			
		}
		
		chanData.setUser(user.getNick(), level, set);
		purgeChan(chan.getName());
		
	}
	
	void delUser(Channel chan, User user) {
		
		AuthChannelData chanData = data.get(chan.getName());
		
		if (chanData == null)
			return;
		
		chanData.delUser(user.getNick());
		purgeChan(chan.getName());
		
	}
	
	private void purgeChan(String chan) {
		
		if (data.get(chan).isEmpty())
			data.remove(chan);
		
	}
	
	void delUserAll(User user) {
		
		for (AuthChannelData chanData : data.values())
			chanData.delUser(user.getNick());
		
	}
	
	UserLevel getLevel(Channel chan, User user) {
		
		AuthChannelData chanData = data.get(chan.getName());
		
		if (chanData == null)
			return null;
		
		return chanData.getLevel(user.getNick());
		
	}
	
}
