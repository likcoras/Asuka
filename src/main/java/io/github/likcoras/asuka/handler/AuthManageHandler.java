package io.github.likcoras.asuka.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.types.GenericUserModeEvent;

import com.google.common.collect.ImmutableMap;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.response.AuthModeRemoveResponse;
import io.github.likcoras.asuka.handler.response.AuthModeResponse;
import io.github.likcoras.asuka.handler.response.AuthWhoResponse;

public class AuthManageHandler extends Handler {

	private static final int RPL_ISUPPORT = 005;
	private static final int RPL_WHOREPLY = 352;

	private Map<Character, UserLevel> prefix;
	private String authChannel;

	public AuthManageHandler(AsukaBot bot) throws ConfigException {
		super(bot);
		prefix = new ConcurrentHashMap<>();
		authChannel = bot.getConfig().getString("authChannel");
	}

	@Override
	public void onServerResponse(ServerResponseEvent<PircBotX> event) {
		if (event.getCode() == RPL_ISUPPORT)
			parseIsupport(event.getParsedResponse());
		else if (event.getCode() == RPL_WHOREPLY)
			parseWhoreply(event.getParsedResponse());
	}

	@Override
	public void onGenericUserMode(GenericUserModeEvent<PircBotX> event) {
		parseModeChange(event.getChannel(), event.getRecipient());
	}

	@Override
	public void onQuit(QuitEvent<PircBotX> event) {
		getBot().send(new AuthModeRemoveResponse(event.getUser()));
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
		case 'q':
			return UserLevel.OWNER;
		case 'a':
			return UserLevel.SUPEROP;
		case 'o':
			return UserLevel.OP;
		case 'h':
			return UserLevel.HALFOP;
		}
		return UserLevel.VOICE;
	}

	private void parseWhoreply(List<String> params) {
		if (params.size() < 3)
			return;
		String channel = params.get(1);
		if (!authChannel.equalsIgnoreCase(channel))
			return;
		String user = params.get(2) + "@" + params.get(3);
		UserLevel level = prefix.get(params.get(6).charAt(params.get(6).length() - 1));
		if (level != null)
			getBot().send(new AuthWhoResponse(user, level));
	}

	private void parseModeChange(Channel channel, User user) {
		if (!authChannel.equalsIgnoreCase(channel.getName()))
			return;
		SortedSet<UserLevel> levels = channel.getUserLevels(user);
		if (levels.isEmpty())
			getBot().send(new AuthModeRemoveResponse(user));
		else
			getBot().send(new AuthModeResponse(user, levels.last()));
	}

}
