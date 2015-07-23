package io.github.likcoras.asuka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.types.GenericUserEvent;

import com.google.common.collect.ImmutableList;

import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.AuthManageHandler;
import io.github.likcoras.asuka.handler.BatotoHandler;
import io.github.likcoras.asuka.handler.Handler;
import io.github.likcoras.asuka.handler.IgnoreManageHandler;
import io.github.likcoras.asuka.handler.MangaUpdatesHandler;
import io.github.likcoras.asuka.handler.QuitHandler;
import io.github.likcoras.asuka.handler.SilentSkyRSSHandler;
import io.github.likcoras.asuka.handler.SilentSkyXMLHandler;
import io.github.likcoras.asuka.handler.UptimeHandler;
import io.github.likcoras.asuka.handler.response.BotResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HandlerManager implements Listener<PircBotX> {

	private AsukaBot bot;
	private List<Handler> handlers;
	private ExecutorService executor;

	public HandlerManager(@NonNull AsukaBot bot) {
		this.bot = bot;
		executor = Executors.newCachedThreadPool();
		addHandlers();
	}

	public void configHandlers(@NonNull BotConfig config) throws ConfigException {
		for (Handler handler : handlers)
			handler.configure(config);
	}

	private void addHandlers() {
		handlers = ImmutableList.<Handler> builder()
				.add(new AuthManageHandler())
				.add(new IgnoreManageHandler())
				.add(new QuitHandler())
				.add(new UptimeHandler())
				.add(new BatotoHandler())
				.add(new MangaUpdatesHandler())
				.add(new SilentSkyXMLHandler())
				.add(new SilentSkyRSSHandler())
				.build();
	}

	@Override
	public void onEvent(Event<PircBotX> event) {
		if (!shouldIgnore(event))
			handle(event);
	}

	@SuppressWarnings("unchecked")
	private boolean shouldIgnore(Event<PircBotX> event) {
		if (!(event instanceof GenericUserEvent))
			return false;
		User user = ((GenericUserEvent<PircBotX>) event).getUser();
		return user != null && bot.getIgnoreManager().isIgnored(user);
	}

	private void handle(Event<PircBotX> event) {
		List<Future<BotResponse>> futures = new ArrayList<>();
		for (Handler handler : handlers)
			futures.add(executor.submit(new HandlerTask(bot, handler, event)));
		sendOutput(futures);
	}

	private void sendOutput(List<Future<BotResponse>> futures) {
		for (Future<BotResponse> future : futures) {
			try {
				future.get().send(bot);
			} catch (ExecutionException e) {
				if (e.getCause() instanceof PermissionException) {
					PermissionException perm = (PermissionException) e.getCause();
					perm.getUser().send().notice("You are not allowed to execute query " + perm.getQuery());
					log.warn("User " + BotUtil.getId(perm.getUser()) + " was denied access when attempting to send " + perm.getQuery());
				} else
					log.error("Exception caught while handling:", e);
			} catch (InterruptedException e) {
				log.error("Interrupted:", e);
			}
		}
	}

}
