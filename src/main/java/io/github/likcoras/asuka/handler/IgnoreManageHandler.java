package io.github.likcoras.asuka.handler;

import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.IgnoreAddResponse;
import io.github.likcoras.asuka.handler.response.IgnoreDelResponse;
import io.github.likcoras.asuka.handler.response.IgnoreHelpResponse;
import io.github.likcoras.asuka.handler.response.IgnoreListResponse;

public class IgnoreManageHandler extends TranslatingHandler {

	@Override
	public BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event) throws PermissionException {
		if (!BotUtil.isTrigger(event.getMessage(), "ignore"))
			return EmptyResponse.get();
		else if (bot.getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			return handleIgnore(event);
		else
			throw new PermissionException(this, event.getUser(), event.getMessage());
	}

	@Override
	public BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event) throws PermissionException {
		if (!BotUtil.isTrigger(event.getMessage(), "ignore"))
			return EmptyResponse.get();
		else if (bot.getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			return handleIgnore(event);
		else
			throw new PermissionException(this, event.getUser(), event.getMessage());
	}

	private BotResponse handleIgnore(GenericMessageEvent<PircBotX> event) {
		List<String> args = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults()
				.splitToList(event.getMessage());
		if (args.size() > 1 && args.get(1).equalsIgnoreCase("list"))
			return new IgnoreListResponse(event);
		else if (args.size() < 3)
			return new IgnoreHelpResponse(event);
		else if (args.get(1).equalsIgnoreCase("add"))
			return new IgnoreAddResponse(event, args.get(2));
		else if (args.get(1).equalsIgnoreCase("del"))
			return new IgnoreDelResponse(event, args.get(2));
		else
			return new IgnoreHelpResponse(event);
	}

}
