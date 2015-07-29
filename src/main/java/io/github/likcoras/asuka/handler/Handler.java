package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.exception.ConfigException;
import lombok.Getter;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;

public abstract class Handler extends ListenerAdapter<PircBotX> {

	@Getter
	private final AsukaBot bot;

	public Handler(AsukaBot bot) throws ConfigException {
		this.bot = bot;
	}

}
