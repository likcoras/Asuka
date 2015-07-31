package io.github.likcoras.asuka;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.net.SocketFactory;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.managers.ListenerManager;

import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.AuthManageHandler;
import io.github.likcoras.asuka.handler.BatotoHandler;
import io.github.likcoras.asuka.handler.GetNickHandler;
import io.github.likcoras.asuka.handler.IgnoreManageHandler;
import io.github.likcoras.asuka.handler.MangaUpdatesHandler;
import io.github.likcoras.asuka.handler.QuitHandler;
import io.github.likcoras.asuka.handler.RawHandler;
import io.github.likcoras.asuka.handler.SilentSkyRSSHandler;
import io.github.likcoras.asuka.handler.SilentSkyXMLHandler;
import io.github.likcoras.asuka.handler.UptimeHandler;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.out.DefaultOutputManager;
import io.github.likcoras.asuka.out.OutputManager;
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
	private OutputManager outputManager;
	@Getter
	private ListenerManager<PircBotX> listenerManager;

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
	}

	private boolean initComponents() {
		config = new BotConfig();
		try {
			if (!config.load(Paths.get("config.txt"))) {
				log.warn("A new configuration file was written, please edit!");
				return false;
			} else if (!checkOldConfig()) {
				log.error(
						"Old configuration detected, please follow the instructions above, or delete the configuration to re-generate one.");
				return false;
			}
			ignoreManager = new IgnoreManager();
			ignoreManager.readFile(Paths.get("ignore.txt"));
			authManager = new AuthManager(config);
			outputManager = new DefaultOutputManager(this);
			listenerManager = new AsukaListenerManager(this);
			addListeners(listenerManager);
		} catch (ConfigException e) {
			log.error("Configuration exception while initializing:", e);
			return false;
		} catch (IOException e) {
			log.error("I/O exception while initializing:", e);
			return false;
		}
		return true;
	}

	private boolean checkOldConfig() throws ConfigException {
		int[] version = Arrays.asList(config.getString("version").replaceAll("-SNAPSHOT", "").split("\\.")).stream()
				.mapToInt(s -> Integer.parseInt(s)).toArray();
		boolean valid = true;
		if (!isHigher(version, new int[] { 3, 0, 5 })) {
			log.error("Change 'authChannels' to 'authChannel' and only allow a single channel. Afterwards, set 'version' to at least 3.0.5");
			valid = false;
		}
		return valid;
	}

	private boolean isHigher(int[] current, int[] required) {
		if (current[0] > required[0])
			return true;
		else if (current[0] == required[0])
			if (current[1] > required[1])
				return true;
			else if (current[1] == required[1])
				if (current[2] >= required[2])
					return true;
		return false;
	}

	private void addListeners(ListenerManager<PircBotX> listenerManager) throws ConfigException {
		listenerManager.addListener(new AuthManageHandler(this));
		listenerManager.addListener(new BatotoHandler(this));
		listenerManager.addListener(new IgnoreManageHandler(this));
		listenerManager.addListener(new GetNickHandler(this));
		listenerManager.addListener(new MangaUpdatesHandler(this));
		listenerManager.addListener(new QuitHandler(this));
		listenerManager.addListener(new RawHandler(this));
		listenerManager.addListener(new SilentSkyRSSHandler(this));
		listenerManager.addListener(new SilentSkyXMLHandler(this));
		listenerManager.addListener(new UptimeHandler(this));
		listenerManager.addListener(new BotLogger());
	}

	private void configureBot() throws ConfigException {
		Configuration.Builder<PircBotX> botConfig = new Configuration.Builder<>().setAutoNickChange(true)
				.setAutoReconnect(true).setAutoSplitMessage(true).setMessageDelay(0L)
				.setListenerManager(listenerManager).setName(config.getString("ircNick"))
				.setLogin(config.getString("ircUsername")).setRealName(config.getString("ircRealname"))
				.setNickservPassword(config.getString("ircPassword"))
				.setServer(config.getString("ircServer"), config.getInt("ircPort"))
				.setSocketFactory(config.getBoolean("ircSSL") ? new UtilSSLSocketFactory().trustAllCertificates()
						: SocketFactory.getDefault());
		Stream<String[]> channels = config.getStringList("ircChannels").stream().map(s -> s.split(":", 2));
		channels.forEach(c -> {
			if (c.length > 1)
				botConfig.addAutoJoinChannel(c[0], c[1]);
			else
				botConfig.addAutoJoinChannel(c[0]);
		});
		ircBot = new PircBotX(botConfig.buildConfiguration());
	}

	public void send(BotResponse response) {
		outputManager.send(response);
	}

}
