package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.response.GetNickResponse;
import io.github.likcoras.asuka.handler.response.PermissionResponse;

public class GetNickHandler extends Handler {

	private String nick;
	private String password;

	public GetNickHandler(AsukaBot bot) throws ConfigException {
		super(bot);
		nick = bot.getConfig().getString("ircNick");
		password = bot.getConfig().getString("ircPassword");
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		if (!BotUtil.isTrigger(event.getMessage(), "getnick"))
			return;
		else if (getBot().getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
			getBot().send(new GetNickResponse(event, nick, password));
		else
			getBot().send(new PermissionResponse(event));
	}

}
