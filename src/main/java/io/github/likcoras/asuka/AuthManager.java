package io.github.likcoras.asuka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import com.google.common.base.Optional;
import io.github.likcoras.asuka.exception.ConfigException;

public class AuthManager {

	private String ownerId;
	private Map<String, UserLevel> levels;

	public AuthManager(BotConfig config) throws ConfigException {
		levels = new ConcurrentHashMap<>();
		ownerId = config.getString("ownerId");
		levels.put(ownerId, UserLevel.OWNER);
	}

	public void setLevel(User user, UserLevel level) {
		setLevel(BotUtil.getId(user), level);
	}

	public void setLevel(String user, UserLevel level) {
		if (user.equals(ownerId))
			return;
		else if (levels.containsKey(user) && levels.get(user).compareTo(level) > 0)
			return;
		levels.put(user, level);
	}

	public void removeLevel(User user) {
		removeLevel(BotUtil.getId(user));
	}

	public void removeLevel(String user) {
		if (!user.equals(ownerId))
			levels.remove(user);
	}

	public Optional<UserLevel> getLevel(User user) {
		return Optional.fromNullable(levels.get(BotUtil.getId(user)));
	}

	public boolean checkAccess(User user, UserLevel required) {
		Optional<UserLevel> userLevel = getLevel(user);
		if (!userLevel.isPresent())
			return false;
		return required.compareTo(userLevel.get()) <= 0;
	}

}
