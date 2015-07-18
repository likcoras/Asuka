package io.github.likcoras.asuka.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import com.google.common.base.Optional;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;

public class AuthManager {
	
	private String ownerId;
	private Map<String, UserLevel> levels;
	
	public AuthManager(BotConfig config) throws ConfigException {
		levels = new ConcurrentHashMap<String, UserLevel>();
		ownerId = config.getString("ownerId");
		levels.put(ownerId, UserLevel.OWNER);
	}
	
	public void setLevel(User user, UserLevel level) {
		String id = BotUtil.getId(user);
		if (!id.equals(ownerId))
			levels.put(id, level);
	}
	
	public void removeLevel(User user) {
		String id = BotUtil.getId(user);
		if (!id.equals(ownerId))
			levels.remove(id);
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
