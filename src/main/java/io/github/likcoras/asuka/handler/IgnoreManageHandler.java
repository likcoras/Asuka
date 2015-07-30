package io.github.likcoras.asuka.handler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.response.IgnoreAddResponse;
import io.github.likcoras.asuka.handler.response.IgnoreDelResponse;
import io.github.likcoras.asuka.handler.response.IgnoreHelpResponse;
import io.github.likcoras.asuka.handler.response.IgnoreListResponse;
import io.github.likcoras.asuka.handler.response.PermissionResponse;

public class IgnoreManageHandler extends Handler {

	private static final Path IGNORE_FILE = Paths.get("ignore.txt");

	public IgnoreManageHandler(AsukaBot bot) {
		super(bot);
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		if (!BotUtil.isTrigger(event.getMessage(), "ignore"))
			return;
		else if (getBot().getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			handleIgnore(event);
		else
			getBot().send(new PermissionResponse(event));
	}

	private void handleIgnore(GenericMessageEvent<PircBotX> event) {
		List<String> args = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults()
				.splitToList(event.getMessage());
		if (args.size() > 1 && args.get(1).equalsIgnoreCase("list"))
			getBot().send(new IgnoreListResponse(event));
		else if (args.size() < 3)
			getBot().send(new IgnoreHelpResponse(event));
		else if (args.get(1).equalsIgnoreCase("add"))
			getBot().send(new IgnoreAddResponse(event, args.get(2), IGNORE_FILE));
		else if (args.get(1).equalsIgnoreCase("del"))
			getBot().send(new IgnoreDelResponse(event, args.get(2), IGNORE_FILE));
		else
			getBot().send(new IgnoreHelpResponse(event));
	}

}
