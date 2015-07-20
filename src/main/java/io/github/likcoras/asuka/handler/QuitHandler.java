package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.QuitResponse;

public class QuitHandler extends TranslatingHandler {
	
	private String quitReply;
	private String quitMessage;
	
	@Override
	public void configure(BotConfig config) throws ConfigException {
		quitReply = config.getString("quitReply");
		quitMessage = config.getString("quitMessage");
	}
	
	@Override
	public BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event) throws PermissionException {
		if (!BotUtil.isTrigger(event.getMessage(), "quit"))
			return EmptyResponse.get();
		else if (bot.getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			return new QuitResponse(event, quitReply, quitMessage);
		else
			throw new PermissionException(this, event.getUser(), event.getMessage());
	}
	
	@Override
	public BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event) throws PermissionException {
		if (!BotUtil.isTrigger(event.getMessage(), "quit"))
			return EmptyResponse.get();
		else if (bot.getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			return new QuitResponse(event, quitReply, quitMessage);
		else
			throw new PermissionException(this, event.getUser(), event.getMessage());
	}
	
}
