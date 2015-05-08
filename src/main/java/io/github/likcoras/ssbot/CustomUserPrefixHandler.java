package io.github.likcoras.ssbot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pircbotx.PircBotX;
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
	private final Map<String, Map<String, UserLevel>> data;
	
	public CustomUserPrefixHandler() {
		
		prefix = new HashMap<Character, UserLevel>();
		prefix.put('~', UserLevel.OWNER);
		prefix.put('&', UserLevel.SUPEROP);
		prefix.put('@', UserLevel.OP);
		prefix.put('%', UserLevel.HALFOP);
		prefix.put('+', UserLevel.VOICE);
		
		data = new HashMap<String, Map<String, UserLevel>>();
		
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
		
		delMode(eve.getChannel().getName(), eve.getUser().getNick());
		
	}
	
	@Override
	public void onKick(final KickEvent<PircBotX> eve) {
		
		delMode(eve.getChannel().getName(), eve.getRecipient().getNick());
		
	}
	
	@Override
	public void onOwner(final OwnerEvent<PircBotX> eve) {
		
		setMode(eve.getChannel().getName(), eve.getRecipient().getNick(),
			UserLevel.OWNER);
		
	}
	
	@Override
	public void onSuperOp(final SuperOpEvent<PircBotX> eve) {
		
		setMode(eve.getChannel().getName(), eve.getRecipient().getNick(),
			UserLevel.SUPEROP);
		
	}
	
	@Override
	public void onOp(final OpEvent<PircBotX> eve) {
		
		setMode(eve.getChannel().getName(), eve.getRecipient().getNick(),
			UserLevel.OP);
		
	}
	
	@Override
	public void onHalfOp(final HalfOpEvent<PircBotX> eve) {
		
		setMode(eve.getChannel().getName(), eve.getRecipient().getNick(),
			UserLevel.HALFOP);
		
	}
	
	@Override
	public void onVoice(final VoiceEvent<PircBotX> eve) {
		
		setMode(eve.getChannel().getName(), eve.getRecipient().getNick(),
			UserLevel.VOICE);
		
	}
	
	public synchronized UserLevel
		getLevel(final String chan, final String user) {
		
		return data.get(chan).get(user);
		
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
		
		final Map<String, UserLevel> userData =
			new HashMap<String, UserLevel>();
		for (final String user : users)
			if (prefix.containsKey(user.charAt(0)))
				userData.put(user.substring(1), prefix.get(user.charAt(0)));
		
		if (!userData.isEmpty())
			data.put(msg.get(2), userData);
		
	}
	
	private synchronized void swapNick(final String old, final String user) {
		
		for (final Entry<String, Map<String, UserLevel>> e : data.entrySet())
			if (e.getValue().containsKey(old)) {
				
				final UserLevel temp = e.getValue().get(old);
				e.getValue().remove(old);
				e.getValue().put(user, temp);
				
			}
		
	}
	
	private synchronized void delMode(final String chan, final String user) {
		
		data.get(chan).remove(user);
		
	}
	
	private synchronized void setMode(final String chan, final String user,
		final UserLevel level) {
		
		data.get(chan).put(user, level);
		
	}
	
}
