package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.handler.response.PermissionResponse;
import io.github.likcoras.asuka.handler.response.RawResponse;

public class RawHandler extends Handler {

	public RawHandler(AsukaBot bot) {
		super(bot);
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		if (!event.getMessage().startsWith("~!"))
			return;
		else if (getBot().getAuthManager().checkAccess(event.getUser(), UserLevel.OWNER))
			getBot().send(new RawResponse(event.getMessage().substring(2)));
		else
			getBot().send(new PermissionResponse(event));
	}

}
