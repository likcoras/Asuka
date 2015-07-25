package io.github.likcoras.asuka.handler.response;

import java.io.IOException;
import java.nio.file.Path;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.exception.ResponseException;
import lombok.NonNull;

public class IgnoreAddResponse implements BotResponse {

	private GenericMessageEvent<PircBotX> event;
	private String user;
	private Path ignoreFile;

	public IgnoreAddResponse(@NonNull GenericMessageEvent<PircBotX> event, @NonNull String user, Path ignoreFile) {
		this.event = event;
		this.user = user;
		this.ignoreFile = ignoreFile;
	}

	@Override
	public void send(AsukaBot bot) throws ResponseException {
		bot.getIgnoreManager().addIgnored(user);
		try {
			bot.getIgnoreManager().writeFile(ignoreFile);
		} catch (IOException e) {
			throw new ResponseException("Error while writing ignroes to file:", e);
		}
		event.getUser().send().notice("User " + user + " added to ignore list");
	}

}
