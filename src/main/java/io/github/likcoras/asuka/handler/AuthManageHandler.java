package io.github.likcoras.asuka.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.types.GenericUserModeEvent;
import com.google.common.collect.ImmutableMap;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.response.AuthModeResponse;
import io.github.likcoras.asuka.handler.response.AuthWhoResponse;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;

public class AuthManageHandler implements Handler {
	
	private static final int RPL_ISUPPORT = 005;
	private static final int RPL_WHOREPLY = 352;
	
	private Map<Character, UserLevel> prefix;
	private List<String> authChannels;
	
	@Override
	public void configure(BotConfig config) throws ConfigException {
		prefix = new ConcurrentHashMap<>();
		authChannels = config.getStringList("authChannels");
	}
	
	@Override
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event) throws HandlerException {
		if (event instanceof ServerResponseEvent) {
			ServerResponseEvent<PircBotX> numericEvent = (ServerResponseEvent<PircBotX>) event;
			if (numericEvent.getCode() == RPL_ISUPPORT)
				parseIsupport(numericEvent.getParsedResponse());
			else if (numericEvent.getCode() == RPL_WHOREPLY)
				return parseWhoreply(numericEvent.getParsedResponse());
		} else if (event instanceof GenericUserModeEvent) {
			@SuppressWarnings("unchecked")
			GenericUserModeEvent<PircBotX> modeEvent = (GenericUserModeEvent<PircBotX>) event;
			if (authChannels.contains(modeEvent.getChannel().getName()))
				return parseModeSet(modeEvent);
		}
		return EmptyResponse.get();
	}
	
	private void parseIsupport(List<String> params) {
		String prefixParam = "";
		for (String param : params)
			if (param.startsWith("PREFIX=")) {
				prefixParam = param;
				break;
			}
		if (prefixParam.isEmpty())
			return;
		char[] modes = prefixParam.substring(prefixParam.indexOf("(") + 1, prefixParam.indexOf(")")).toCharArray();
		char[] prefixes = prefixParam.substring(prefixParam.indexOf(")") + 1).toCharArray();
		ImmutableMap.Builder<Character, UserLevel> builder = ImmutableMap.builder();
		for (int i = 0; i < modes.length; i++)
			builder.put(prefixes[i], levelFromMode(modes[i]));
		prefix = builder.build();
	}
	
	private UserLevel levelFromMode(char modeChar) {
		switch (modeChar) {
			case 'q': return UserLevel.OWNER;
			case 'a': return UserLevel.SUPEROP;
			case 'o': return UserLevel.OP;
			case 'h': return UserLevel.HALFOP;
		}
		return UserLevel.VOICE;
	}
	
	private BotResponse parseWhoreply(List<String> params) {
		if (params.size() < 3)
			return EmptyResponse.get();
		String channel = params.get(2);
		if (!authChannels.contains(channel))
			return EmptyResponse.get();
		String user = params.get(3) + "@" + params.get(4);
		UserLevel level = prefix.get(params.get(7).charAt(params.get(7).length() - 1));
		if (level != null)
			return new AuthWhoResponse(user, level);
		return EmptyResponse.get();
	}
	
	private BotResponse parseModeSet(GenericUserModeEvent<PircBotX> modeEvent) {
		Channel channel = modeEvent.getChannel();
		if (!authChannels.contains(channel.getName()))
			return EmptyResponse.get();
		User user = modeEvent.getRecipient();
		return new AuthModeResponse(user, channel.getUserLevels(user).last());
	}
	
}
