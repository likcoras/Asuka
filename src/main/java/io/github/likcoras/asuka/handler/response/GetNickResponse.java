package io.github.likcoras.asuka.handler.response;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ResponseException;

public class GetNickResponse implements BotResponse {

	private static final String FORMAT = "Attempted to recover nick ";

	private GenericMessageEvent<PircBotX> event;
	private String nick;
	private String password;

	public GetNickResponse(GenericMessageEvent<PircBotX> event, String nick, String password) {
		this.event = event;
		this.nick = nick;
		this.password = password;
	}

	@Override
	public void send(AsukaBot bot) throws ResponseException {
		bot.getIrcBot().sendRaw().rawLine("NICKSERV RECOVER " + nick + " " + password);
		bot.getIrcBot().sendRaw().rawLine("NICKSERV RELEASE " + nick + " " + password);
		bot.getIrcBot().sendIRC().changeNick(nick);
		bot.getIrcBot().sendIRC().identify(password);
		BotUtil.chanUserRespond(event, FORMAT + nick);
	}

}
