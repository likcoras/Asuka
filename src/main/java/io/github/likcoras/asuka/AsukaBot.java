package io.github.likcoras.asuka;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.net.SocketFactory;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;

import io.github.likcoras.asuka.exception.ConfigException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
		log.info("Loading configuration...");
		if (!initComponents()) {
			log.info("Exiting.");
			return;
		}
		try {
			configureBot();
		} catch (ConfigException e) {
			log.error("Configuration exception while configuring irc bot:", e);
			return;
		}
		log.info("Configuration loaded. Starting bot...");
		try {
			ircBot.startBot();
		} catch (IOException | IrcException e) {
			log.error("Exception caught while running bot: ", e);
		}
		handlerManager.shutdown();
	}

	private boolean initComponents() {
		config = new BotConfig();
		try {
			if (!config.load(Paths.get("config.txt"))) {
				log.warn("A new configuration file was written, please edit!");
				return false;
			}
			ignoreManager = new IgnoreManager();
			authManager = new AuthManager(config);
			handlerManager = new HandlerManager(this);
			handlerManager.configHandlers(config);
		} catch (ConfigException e) {
			log.error("Configuration exception while initializing:", e);
			return false;
		} catch (IOException e) {
			log.error("I/O exception while initializing:", e);
			return false;
		}
		return true;
	}

	private void configureBot() throws ConfigException {
		Configuration.Builder<PircBotX> botConfig = new Configuration.Builder<PircBotX>()
				.addListener(handlerManager)
				.addListener(new BotLogger())
				.setAutoNickChange(true)
				.setAutoReconnect(true)
				.setAutoSplitMessage(true)
				.setMessageDelay(0L)
				.setName(config.getString("ircNick"))
				.setLogin(config.getString("ircUsername"))
				.setRealName(config.getString("ircRealname"))
				.setNickservPassword(config.getString("ircPassword"))
				.setServer(config.getString("ircServer"), config.getInt("ircPort"))
				.setSocketFactory(config.getBoolean("ircSSL") ? new UtilSSLSocketFactory().trustAllCertificates()
						: SocketFactory.getDefault());
		Stream<String[]> channels = config.getStringList("ircChannels").stream().map(s -> s.split(":"));
		channels.forEach(c -> {
			if (c.length > 1)
				botConfig.addAutoJoinChannel(c[0], c[1]);
			else
				botConfig.addAutoJoinChannel(c[0]);
		});
		ircBot = new PircBotX(botConfig.buildConfiguration());
	}

}
