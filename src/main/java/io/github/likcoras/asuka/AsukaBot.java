package io.github.likcoras.asuka;

import java.io.IOException;
import java.nio.file.Paths;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import io.github.likcoras.asuka.exception.ConfigException;
import lombok.Getter;

public class AsukaBot {
	
	@Getter
	private BotConfig config;
	@Getter
	private PircBotX ircBot;
	@Getter
	private AuthManager authManager;
	@Getter
	private IgnoreManager ignoreManager;
	@Getter
	private HandlerManager handlerManager;
	
	public static void main(String[] args) {
		new AsukaBot();
	}
	
	private AsukaBot() {
		if (!initComponents())
			return; // TODO inform user
		try {
			configureBot();
		} catch (ConfigException e) {
			// TODO inform user
		}
		try {
			ircBot.startBot();
		} catch (IOException | IrcException e) {
			// TODO inform user
		}
	}
	
	private boolean initComponents() {
		config = new BotConfig();
		try {
			if (!config.load(Paths.get("config.txt")))
				return false; // TODO inform user
			ignoreManager = new IgnoreManager();
			authManager = new AuthManager(config);
			handlerManager = new HandlerManager(config);
		} catch (ConfigException e) {
			return false; // TODO inform user
		} catch (IOException e) {
			return false; // TODO inform user
		}
		return true;
	}
	
	private void configureBot() throws ConfigException {
		Configuration.Builder<PircBotX> botConfig = new Configuration.Builder<PircBotX>()
				.addListener(handlerManager)
				.setAutoNickChange(true)
				.setAutoReconnect(true)
				.setAutoSplitMessage(true)
				.setCapEnabled(true)
				.setName(config.getString("ircNick"))
				.setLogin(config.getString("ircUsername"))
				.setRealName(config.getString("ircRealname"))
				.setNickservPassword("ircPassword")
				.setServer(config.getString("ircServer"), config.getInt("ircPort"));
		config.getStringList("ircChannels").forEach(s -> botConfig.addAutoJoinChannel(s));
		ircBot = new PircBotX(botConfig.buildConfiguration());
	}
	
}
