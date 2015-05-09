package io.github.likcoras.ssbot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.HalfOpEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.OwnerEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SuperOpEvent;
import org.pircbotx.hooks.events.VoiceEvent;

public class CustomUserPrefixHandler extends ListenerAdapter<PircBotX> {
	
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
	
	@Override
	public void onServerResponse(final ServerResponseEvent<PircBotX> eve) {
		
		if (eve.getCode() == 005)
			parsePrefix(eve.getParsedResponse());
		else if (eve.getCode() == 353)
			parseNames(eve.getParsedResponse());
		
	}
	
	@Override
	public void onNickChange(final NickChangeEvent<PircBotX> eve) {
		
		swapNick(eve.getOldNick(), eve.getNewNick());
		
	}
	
	@Override
	public void onPart(final PartEvent<PircBotX> eve) {
		
		delUser(eve.getUser(), eve.getChannel());
		
	}
	
	@Override
	public void onKick(final KickEvent<PircBotX> eve) {
		
		delUser(eve.getRecipient(), eve.getChannel());
		
	}
	
	@Override
	public void onOwner(final OwnerEvent<PircBotX> eve) {
		
		modUser(eve.getRecipient(), eve.getChannel(), UserLevel.OWNER, eve.isOwner());
		
	}
	
	@Override
	public void onSuperOp(final SuperOpEvent<PircBotX> eve) {
		
		modUser(eve.getRecipient(), eve.getChannel(), UserLevel.SUPEROP, eve.isSuperOp());
		
	}
	
	@Override
	public void onOp(final OpEvent<PircBotX> eve) {
		modUser(eve.getRecipient(), eve.getChannel(), UserLevel.OP, eve.isOp());
		
	}
	
	@Override
	public void onHalfOp(final HalfOpEvent<PircBotX> eve) {
		
		modUser(eve.getRecipient(), eve.getChannel(), UserLevel.HALFOP, eve.isHalfOp());
		
	}
	
	@Override
	public void onVoice(final VoiceEvent<PircBotX> eve) {
		
		modUser(eve.getRecipient(), eve.getChannel(), UserLevel.VOICE, eve.hasVoice());
		
	}
	
	public synchronized UserLevel getLevel(final User user, final Channel chan) {
		
		return getHighest(user, chan);
		
	}
	
	private synchronized void parsePrefix(final List<String> msg) {
		
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
	
	private synchronized void parseNames(final List<String> msg) {
		
		final String[] users = msg.get(3).split(" ");
		
		if (users.length < 1)
			return;
		
		final Map<String, Set<UserLevel>> userData =
			new HashMap<String, Set<UserLevel>>();
		
		for (final String user : users)
			if (prefix.containsKey(user.charAt(0))) {
				
				Set<UserLevel> levels = new HashSet<UserLevel>();
				levels.add(prefix.get(user.charAt(0)));
				userData.put(user.substring(1), levels);
				
			}
		
		if (!userData.isEmpty())
			data.put(msg.get(2), userData);
		
	}
	
	private synchronized void modUser(User user, Channel chan, UserLevel level, boolean get) {
		
		if (get)
			setMode(user, chan, level);
		else
			delMode(user, chan, level);
		
	}
	
	private synchronized Map<String, Set<UserLevel>> getChan(Channel chan) {
		
		Map<String, Set<UserLevel>> chanData = data.get(chan.getName());
		
		if (chanData == null) {
			
			chanData = new HashMap<String, Set<UserLevel>>();
			data.put(chan.getName(), chanData);
			
		}
		
		return chanData;
		
	}
	
	private synchronized Set<UserLevel> getUser(Map<String, Set<UserLevel>> chan, User user) {
		
		Set<UserLevel> userLevels = chan.get(user.getNick());
		
		if (userLevels == null) {
			
			userLevels = new HashSet<UserLevel>();
			chan.put(user.getNick(), userLevels);
			
		}
		
		return userLevels;
		
	}
	
	private synchronized void swapNick(final String old, final String user) {
		
		for (final Entry<String, Map<String, Set<UserLevel>>> e : data.entrySet()) {
			
			Map<String, Set<UserLevel>> chanData = e.getValue();
			
			if (chanData.containsKey(old)) {
				
				final Set<UserLevel> temp = chanData.get(old);
				chanData.remove(old);
				chanData.put(user, temp);
				
			}
			
		}
		
	}
	
	private synchronized void delUser(final User user, final Channel chan) {
		
		getChan(chan).remove(user.getNick());
		
	}
	
	private synchronized void delMode(final User user, final Channel chan, UserLevel level) {
		
		getUser(getChan(chan), user).remove(level);
		
	}
	
	private synchronized void setMode(final User user, final Channel chan,
		final UserLevel level) {
		
		Set<UserLevel> levels = getUser(getChan(chan), user);
		levels.add(level);
		
		getChan(chan).put(user.getNick(), levels);
		
	}
	
	private synchronized UserLevel getHighest(User user, Channel chan) {
		
		Set<UserLevel> userLevels = getUser(getChan(chan), user);
		
		UserLevel level = null;
		for (UserLevel current : userLevels)
			if (level == null || level.compareTo(current) < 0)
				level = current;
		
		return level;
		
	}
	
}
