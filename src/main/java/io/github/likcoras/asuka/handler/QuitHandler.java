package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.types.GenericMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.QuitResponse;

public class QuitHandler implements Handler {
	
	private String quitReply;
	private String quitMessage;
	
	@Override
	public void configure(BotConfig config) throws ConfigException {
		quitReply = config.getString("quitReply");
		quitMessage = config.getString("quitMessage");
	}
	
	@Override
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event) throws HandlerException {
		if (!(event instanceof GenericMessageEvent))
			return EmptyResponse.get();
		@SuppressWarnings("unchecked")
		GenericMessageEvent<PircBotX> messageEvent = (GenericMessageEvent<PircBotX>) event;
		String message = messageEvent.getMessage();
		User user = messageEvent.getUser();
		if (BotUtil.isTrigger(message, "quit"))
			if (bot.getAuthManager().checkAccess(user, UserLevel.OP))
				return new QuitResponse(messageEvent, quitReply, quitMessage);
			else
				throw new PermissionException(this, user, message);
		return EmptyResponse.get();
	}
	
}
