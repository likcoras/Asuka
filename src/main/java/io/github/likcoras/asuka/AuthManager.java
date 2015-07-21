package io.github.likcoras.asuka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import com.google.common.base.Optional;
import io.github.likcoras.asuka.exception.ConfigException;
import lombok.NonNull;

public class AuthManager {

	private String ownerId;
	private Map<String, UserLevel> levels;

	public AuthManager(@NonNull BotConfig config) throws ConfigException {
		levels = new ConcurrentHashMap<String, UserLevel>();
		ownerId = config.getString("ownerId");
		levels.put(ownerId, UserLevel.OWNER);
	}

	public void setLevel(@NonNull User user, @NonNull UserLevel level) {
		setLevel(BotUtil.getId(user), level);
	}

	public void setLevel(@NonNull String user, @NonNull UserLevel level) {
		if (user.equals(ownerId))
			return;
		else if (levels.containsKey(user) && levels.get(user).compareTo(level) < 0)
			levels.put(user, level);
	}

	public void removeLevel(@NonNull User user) {
		removeLevel(BotUtil.getId(user));
	}

	public void removeLevel(@NonNull String user) {
		if (!user.equals(ownerId))
			levels.remove(user);
	}

	public Optional<UserLevel> getLevel(@NonNull User user) {
		return Optional.fromNullable(levels.get(BotUtil.getId(user)));
	}

	public boolean checkAccess(@NonNull User user, @NonNull UserLevel required) {
		Optional<UserLevel> userLevel = getLevel(user);
		if (!userLevel.isPresent())
			return false;
		return required.compareTo(userLevel.get()) <= 0;
	}

}
