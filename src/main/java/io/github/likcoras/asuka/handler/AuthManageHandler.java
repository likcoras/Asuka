package io.github.likcoras.asuka.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.HalfOpEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.OwnerEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SuperOpEvent;
import org.pircbotx.hooks.events.VoiceEvent;
import com.google.common.collect.ImmutableMap;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.response.AuthModeResponse;
import io.github.likcoras.asuka.handler.response.AuthWhoResponse;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;

public class AuthManageHandler extends TranslatingHandler {
	
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
	public BotResponse onServerResponse(AsukaBot bot, ServerResponseEvent<PircBotX> event) {
		if (event.getCode() == RPL_ISUPPORT)
			parseIsupport(event.getParsedResponse());
		else if (event.getCode() == RPL_WHOREPLY)
			return parseWhoreply(event.getParsedResponse());
		return EmptyResponse.get();
	}
	
	@Override
	public BotResponse onVoice(AsukaBot bot, VoiceEvent<PircBotX> event) {
		return parseModeSet(event.getChannel(), event.getRecipient(), UserLevel.VOICE);
	}
	
	@Override
	public BotResponse onHalfOp(AsukaBot bot, HalfOpEvent<PircBotX> event) {
		return parseModeSet(event.getChannel(), event.getRecipient(), UserLevel.HALFOP);
	}
	
	@Override
	public BotResponse onOp(AsukaBot bot, OpEvent<PircBotX> event) {
		return parseModeSet(event.getChannel(), event.getRecipient(), UserLevel.OP);
	}
	
	@Override
	public BotResponse onSuperOp(AsukaBot bot, SuperOpEvent<PircBotX> event) {
		return parseModeSet(event.getChannel(), event.getRecipient(), UserLevel.SUPEROP);
	}
	
	@Override
	public BotResponse onOwner(AsukaBot bot, OwnerEvent<PircBotX> event) {
		return parseModeSet(event.getChannel(), event.getRecipient(), UserLevel.OWNER);
	}
	
	private void parseIsupport(List<String> params) {
		Optional<String> prefixParam = params.stream().filter(s -> s.startsWith("PREFIX=")).findAny();
		if (!prefixParam.isPresent())
			return;
		String prefixList = prefixParam.get();
		char[] modes = prefixList.substring(prefixList.indexOf("(") + 1, prefixList.indexOf(")")).toCharArray();
		char[] prefixes = prefixList.substring(prefixList.indexOf(")") + 1).toCharArray();
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
	
	private BotResponse parseModeSet(Channel channel, User user, UserLevel level) {
		if (!authChannels.contains(channel.getName()))
			return EmptyResponse.get();
		return new AuthModeResponse(user, level);
	}
	
}
