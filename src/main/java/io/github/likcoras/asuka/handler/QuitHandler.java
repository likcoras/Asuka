package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.response.PermissionResponse;
import io.github.likcoras.asuka.handler.response.QuitResponse;

public class QuitHandler extends Handler {

	private String quitReply;
	private String quitMessage;

	public QuitHandler(AsukaBot bot) throws ConfigException {
		super(bot);
		quitReply = bot.getConfig().getString("quitReply");
		quitMessage = bot.getConfig().getString("quitMessage");
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		if (!BotUtil.isTrigger(event.getMessage(), "quit " + event.getBot().getNick()))
			return;
		else if (getBot().getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			getBot().send(new QuitResponse(event, quitReply, quitMessage));
		else
			getBot().send(new PermissionResponse(event));
	}

}
