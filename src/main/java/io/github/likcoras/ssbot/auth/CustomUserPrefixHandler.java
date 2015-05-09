package io.github.likcoras.ssbot.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;

public class CustomUserPrefixHandler {
	
	static CustomUserPrefixHandler instance;
	
	private final Map<Character, UserLevel> prefix;
	private final Map<String, Map<String, Set<UserLevel>>> data;
	
	public CustomUserPrefixHandler() {
		
		prefix = new HashMap<Character, UserLevel>();
		prefix.put('~', UserLevel.OWNER);
		prefix.put('&', UserLevel.SUPEROP);
		prefix.put('@', UserLevel.OP);
		prefix.put('%', UserLevel.HALFOP);
		prefix.put('+', UserLevel.VOICE);
		
		data = new HashMap<String, Map<String, Set<UserLevel>>>();
		
	}
	
	public synchronized UserLevel getLevel(final User user, final Channel chan) {
		
		return getHighest(user, chan);
		
	}
	
	synchronized void parsePrefix(final List<String> msg) {
		
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
	
	synchronized void parseNames(final List<String> msg) {
		
		final String[] users = msg.get(3).split(" ");
		
		if (users.length < 1)
			return;
		
		final Map<String, Set<UserLevel>> userData =
			new HashMap<String, Set<UserLevel>>();
		
		for (final String user : users)
			if (prefix.containsKey(user.charAt(0))) {
				
				final Set<UserLevel> levels = new HashSet<UserLevel>();
				levels.add(prefix.get(user.charAt(0)));
				userData.put(user.substring(1), levels);
				
			}
		
		if (!userData.isEmpty())
			data.put(msg.get(2), userData);
		
	}
	
	synchronized void modUser(final User user, final Channel chan,
		final UserLevel level, final boolean get) {
		
		if (get)
			setMode(user, chan, level);
		else
			delMode(user, chan, level);
		
	}
	
	synchronized void swapNick(final String old, final String user) {
		
		for (final Entry<String, Map<String, Set<UserLevel>>> e : data
			.entrySet()) {
			
			final Map<String, Set<UserLevel>> chanData = e.getValue();
			
			if (chanData.containsKey(old)) {
				
				final Set<UserLevel> temp = chanData.get(old);
				chanData.remove(old);
				chanData.put(user, temp);
				
			}
			
		}
		
	}
	
	synchronized void delUser(final User user, final Channel chan) {
		
		getChan(chan).remove(user.getNick());
		
	}
	
	private synchronized void setPrefix(final String prefixMsg) {
		
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
	
	private synchronized UserLevel levelForMode(final char mode) {
		
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
	
	private synchronized Map<String, Set<UserLevel>>
		getChan(final Channel chan) {
		
		Map<String, Set<UserLevel>> chanData = data.get(chan.getName());
		
		if (chanData == null) {
			
			chanData = new HashMap<String, Set<UserLevel>>();
			data.put(chan.getName(), chanData);
			
		}
		
		return chanData;
		
	}
	
	private synchronized Set<UserLevel> getUser(
		final Map<String, Set<UserLevel>> chan, final User user) {
		
		Set<UserLevel> userLevels = chan.get(user.getNick());
		
		if (userLevels == null) {
			
			userLevels = new HashSet<UserLevel>();
			chan.put(user.getNick(), userLevels);
			
		}
		
		return userLevels;
		
	}
	
	private synchronized void delMode(final User user, final Channel chan,
		final UserLevel level) {
		
		getUser(getChan(chan), user).remove(level);
		
	}
	
	private synchronized void setMode(final User user, final Channel chan,
		final UserLevel level) {
		
		final Set<UserLevel> levels = getUser(getChan(chan), user);
		levels.add(level);
		
		getChan(chan).put(user.getNick(), levels);
		
	}
	
	private synchronized UserLevel getHighest(final User user,
		final Channel chan) {
		
		final Set<UserLevel> userLevels = getUser(getChan(chan), user);
		
		UserLevel level = null;
		for (final UserLevel current : userLevels)
			if (level == null || level.compareTo(current) < 0)
				level = current;
		
		return level;
		
	}
	
}
