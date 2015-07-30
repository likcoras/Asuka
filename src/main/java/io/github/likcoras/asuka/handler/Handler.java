package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import lombok.Getter;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;

public abstract class Handler extends ListenerAdapter<PircBotX> {

	@Getter
	private final AsukaBot bot;

	public Handler(AsukaBot bot) {
		this.bot = bot;
	}

}
