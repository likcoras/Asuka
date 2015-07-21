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
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HandlerManager implements Listener<PircBotX> {

	private AsukaBot bot;
	private List<Handler> handlers;
	
	public HandlerManager(AsukaBot bot) {
		this.bot = bot;
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
				handler.handle(bot, event).send(bot);
			} catch (PermissionException e) {
				log.warn("User " + BotUtil.getId(e.getUser()) + " was denied access for query" + e.getQuery());
				e.getUser().send().notice("You are not allowed to execute query " + e.getQuery());
			} catch (HandlerException e) {
				if (event instanceof GenericChannelEvent || event instanceof GenericUserEvent)
					event.respond("Error while handling " + event.getClass().getSimpleName() + " with handler "
							+ handler.getClass().getSimpleName() + ": " + e.getCause().toString());
				log.warn("Exception caught while handling " + event.getClass().getSimpleName() + " with handler "
						+ handler.getClass().getSimpleName() + ": ", e);
			}
	}

}
