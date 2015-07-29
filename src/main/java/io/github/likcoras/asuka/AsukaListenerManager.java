package io.github.likcoras.asuka;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import org.pircbotx.hooks.types.GenericUserEvent;

public class AsukaListenerManager extends ThreadedListenerManager<PircBotX> {

	private AsukaBot bot;

	public AsukaListenerManager(AsukaBot bot) {
		this.bot = bot;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchEvent(Event<PircBotX> event) {
		if (!(event instanceof GenericUserEvent)
				|| !bot.getIgnoreManager().isIgnored(((GenericUserEvent<PircBotX>) event).getUser()))
			super.dispatchEvent(event);
	}

}
