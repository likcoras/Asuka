package io.github.likcoras.asuka;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.hooks.types.GenericUserEvent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.SneakyThrows;

public class AsukaListenerManager extends ThreadedListenerManager<PircBotX> {

	private AsukaBot bot;
	private LoadingCache<String, Integer> lastOut = CacheBuilder.newBuilder().expireAfterWrite(3L, TimeUnit.SECONDS)
			.build(new CacheLoader<String, Integer>() {
				public Integer load(String key) {
					return 1;
				}
			});

	public AsukaListenerManager(AsukaBot bot) {
		this.bot = bot;
	}

	@SneakyThrows(ExecutionException.class)
	@SuppressWarnings("unchecked")
	@Override
	public void dispatchEvent(Event<PircBotX> event) {
		if (event instanceof GenericUserEvent) {
			GenericUserEvent<PircBotX> userEvent = (GenericUserEvent<PircBotX>) event;
			User user = userEvent.getUser();
			if (!bot.getAuthManager().checkAccess(user, UserLevel.VOICE)
					&& event instanceof GenericMessageEvent) {
				String nick = user.getNick();
				int count = lastOut.get(nick);
				if (count >= 3)
					bot.getIgnoreManager().addIgnored(user);
				else
					lastOut.put(nick, count + 1);
			}
			if (bot.getIgnoreManager().isIgnored(user))
				return;
		}
		super.dispatchEvent(event);
	}

}
