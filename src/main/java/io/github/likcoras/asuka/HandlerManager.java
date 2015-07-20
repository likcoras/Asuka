package io.github.likcoras.asuka;

import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.types.GenericChannelEvent;
import org.pircbotx.hooks.types.GenericUserEvent;

import com.google.common.collect.ImmutableList;

import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.AuthManageHandler;
import io.github.likcoras.asuka.handler.BatotoHandler;
import io.github.likcoras.asuka.handler.Handler;
import io.github.likcoras.asuka.handler.IgnoreManageHandler;
import io.github.likcoras.asuka.handler.QuitHandler;
import io.github.likcoras.asuka.handler.UptimeHandler;

public class HandlerManager implements Listener<PircBotX> {

	private AsukaBot bot;
	private List<Handler> handlers;
	
	public HandlerManager(AsukaBot bot) {
		addHandlers();
	}
	
	public void configHandlers(BotConfig config) throws ConfigException {
		for (Handler handler : handlers)
			handler.configure(config);
	}
	
	private void addHandlers() {
		handlers = ImmutableList.<Handler>builder()
				.add(new AuthManageHandler())
				.add(new IgnoreManageHandler())
				.add(new QuitHandler())
				.add(new UptimeHandler())
				.add(new BatotoHandler())
				.build();
	}
	
	@Override
	public void onEvent(Event<PircBotX> event) {
		for (Handler handler : handlers)
			try {
				handler.handle(bot, event);
			} catch (PermissionException e) {
				e.getUser().send().notice("You are not allowed to execute query " + e.getQuery()); // TODO log
			} catch (HandlerException e) {
				if (event instanceof GenericChannelEvent || event instanceof GenericUserEvent)
					event.respond("Error while handling " + event.getClass().getName() + " with handler "
							+ handler.getClass().getName() + ": " + e.getCause().toString());
				// TODO log
			}
	}

}
