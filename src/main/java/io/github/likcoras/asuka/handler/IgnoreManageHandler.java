package io.github.likcoras.asuka.handler;

import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.IgnoreAddResponse;
import io.github.likcoras.asuka.handler.response.IgnoreDelResponse;
import io.github.likcoras.asuka.handler.response.IgnoreHelpResponse;
import io.github.likcoras.asuka.handler.response.IgnoreListResponse;

public class IgnoreManageHandler implements Handler {
	
	@Override
	public void configure(BotConfig config) {}
	
	@Override
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event) throws HandlerException {
		if (!(event instanceof GenericMessageEvent))
			return new EmptyResponse();
		@SuppressWarnings("unchecked")
		GenericMessageEvent<PircBotX> messageEvent = (GenericMessageEvent<PircBotX>) event;
		if (BotUtil.isTrigger(messageEvent.getMessage(), "ignore"))
			if (bot.getAuthManager().checkAccess(messageEvent.getUser(), UserLevel.OP))
				return handleIgnore(messageEvent);
			else
				throw new PermissionException(this, messageEvent.getUser(), messageEvent.getMessage());
		return new EmptyResponse();
	}
	
	public BotResponse handleIgnore(GenericMessageEvent<PircBotX> event) {
		List<String> args = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(event.getMessage());
		if (args.size() > 1 && args.get(1).equalsIgnoreCase("list"))
			return new IgnoreListResponse(event);
		if (args.size() < 3)
			return new IgnoreHelpResponse(event);
		else if (args.get(1).equalsIgnoreCase("add"))
			return new IgnoreAddResponse(event, args.get(2));
		else if (args.get(1).equalsIgnoreCase("del"))
			return new IgnoreDelResponse(event, args.get(2));
		else
			return new IgnoreHelpResponse(event);
	}
	
}
