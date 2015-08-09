package io.github.likcoras.asuka;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import io.github.likcoras.asuka.exception.ConfigException;

public class AuthManager {

	private String authChannel;

	public AuthManager(BotConfig config) throws ConfigException {
		authChannel = config.getString("authChannel");
	}

	public boolean checkAccess(User user, UserLevel required) {
		Channel channel = user.getBot().getUserChannelDao().getChannel(authChannel);
		UserLevel userLevel = channel.getUserLevels(user).last();
		if (userLevel == null)
			return false;
		return required.compareTo(userLevel) <= 0;
	}

}
