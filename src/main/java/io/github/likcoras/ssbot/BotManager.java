package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.DbHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.SocketConnectEvent;

public class BotManager extends ListenerAdapter<PircBotX> {
	
	private final DbHandler database;
	private final MuHandler mangaUpdates;
	private final XmlHandler xmlFile;
	private final BttHandler batoto;
	
	private final PircBotX bot;
	
	private final String password;
	
	private final Pattern mangeUpdatePattern;
	private final Pattern batotoPattern;
	
	public BotManager(final ConfigParser cfg, final DbHandler db,
			final MuHandler mu, final XmlHandler xml, final BttHandler bt) {
		
		database = db;
		mangaUpdates = mu;
		xmlFile = xml;
		batoto = bt;
		
		password = cfg.getProperty("adminpass");
		
		mangeUpdatePattern =
				Pattern.compile("(http(s)?://)?(www\\.)?((rlstrackr.com/series/info/)|(mangaupdates.com/series.html\\?id=))(\\d+)(/)?");
		batotoPattern =
				Pattern.compile("((http(s)?://)?bato.to/comic/_/(comics/)?\\S+(/)?)");
		
		bot = new PircBotX(configure(cfg));
		
	}
	
	public void start() {
		
		try {
			
			bot.startBot();
			
		} catch (IOException | IrcException e) {
			
			System.out.println("Error: Exception with the irc connection!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public void onConnect(final ConnectEvent<PircBotX> eve) {
		
		System.out.println("Connected!");
		
	}
	
	@Override
	public void onDisconnect(final DisconnectEvent<PircBotX> eve) {
		
		System.out.println("Disconnected!");
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> eve) {
		
		final String msg = eve.getMessage();
		
		final Matcher muMatcher = mangeUpdatePattern.matcher(msg);
		final Matcher btMatcher = batotoPattern.matcher(msg);
		
		if (muMatcher.find()) {
			sendMu(eve.getChannel(),
					mangaUpdates.getData(Integer.parseInt(muMatcher.group(7))));
		} else if (btMatcher.find()) {
			sendBt(eve.getChannel(), batoto.getData(btMatcher.group(1)));
		} else if (msg.startsWith("!batoto ") || msg.startsWith(".batoto ")) {
			searchBt(eve.getChannel(), msg.substring(8));
		} else if (msg.startsWith("!baka ") || msg.startsWith(".baka ")) {
			searchMu(eve.getChannel(), msg.substring(6));
		} else if (msg.equals("!quit") || msg.equals(".quit")) {
			quit(eve.getUser(), eve.getChannel());
		} else if (msg.equals("!update") || msg.equals(".update")) {
			update(eve.getUser(), eve.getChannel());
		} else if (msg.startsWith("!")) {
			searchXml(eve.getChannel(), msg.substring(1));
		}
		
	}
	
	@Override
	public void onPrivateMessage(final PrivateMessageEvent<PircBotX> eve) {
		
		String msg = eve.getMessage();
		
		if (msg.equals("quit " + password)) {
			quit(eve.getUser());
		} else if (msg.equals("update " + password)) {
			update(eve.getUser());
		} else {
			eve.getUser().send().message("Nope! It's 'quit <password>' or 'update <password>'!");
		}
		
	}
	
	@Override
	public void onSocketConnect(final SocketConnectEvent<PircBotX> eve) {
		System.out.println("Connecting...");
	}
	
	private Configuration<PircBotX> configure(final ConfigParser cfg) {
		
		final Builder<PircBotX> build = new Configuration.Builder<PircBotX>();
		
		build.addListener(this)
				.setAutoReconnect(true)
				.setName(cfg.getProperty("ircnick"))
				.setLogin(cfg.getProperty("irclogin"))
				.setRealName(cfg.getProperty("ircrealname"))
				.setServer(cfg.getProperty("irchost"),
						Integer.parseInt(cfg.getProperty("ircport")));
		
		if (Boolean.parseBoolean(cfg.getProperty("ircssl"))) {
			build.setSocketFactory(new UtilSSLSocketFactory()
					.trustAllCertificates());
		}
		
		if (!cfg.getProperty("ircnickserv").isEmpty()) {
			build.setNickservPassword(cfg.getProperty("ircnickserv"));
		}
		
		if (!cfg.getProperty("ircpass").isEmpty()) {
			build.setNickservPassword(cfg.getProperty("ircpass"));
		}
		
		final String[] chans = cfg.getProperty("ircchannels").split(",");
		for (final String chan : chans) {
			
			final String[] chanKey = chan.split(":");
			
			if (chanKey.length > 1) {
				build.addAutoJoinChannel(chanKey[0], chanKey[1]);
			} else {
				build.addAutoJoinChannel(chan);
			}
			
		}
		
		return build.buildConfiguration();
		
	}
	
	private void sendBt(final Channel ch, final String[] data) {
		
		ch.send()
				.message(
						String.format(
								("%bName: %b%s | "
										+ "%bAuthor: %b%s | %bTags: %b%s | %bStatus: %b%s | %bLink: %b%s")
												.replaceAll("%b", Colors.BOLD),
								data[0], data[1], data[2], data[3], data[4],
								data[4]));
		
	}
	
	private void sendMu(final Channel ch, final String[] data) {
		
		ch.send()
				.message(
						String.format(
								("%bName: %b%s | "
										+ "%bAuthor: %b%s | %bTags: %b%s | %bLatest Release: %b%s "
										+ "by %s (%s days ago) | %bLink: %b%s")
												.replaceAll("%b", Colors.BOLD),
								data[0], data[1], data[2], data[3], data[5],
								data[4], data[6]));
		
	}
	
	private void sendXml(final Channel ch, final String[] data) {
		
		ch.send().message(
				String.format(
						"%b%s%b chapter %b%s%b | %bMega: %b%s | %bMediafire: %b%s | %bReader: %b%s"
								.replaceAll("%b", Colors.BOLD), data[0],
						data[1], data[2], data[3], data[4]));
		
	}
	
	private void searchBt(final Channel c, final String msg) {
		
		final String[] data = batoto.search(msg);
		
		if (data != null) {
			sendBt(c, data);
		} else {
			c.send().message("Nothing found for '" + msg + "'");
		}
		
	}
	
	private void searchMu(final Channel c, final String msg) {
		
		final int i = database.getId(msg.split(" "));
		
		if (i > -1) {
			sendMu(c, mangaUpdates.getData(i));
		} else {
			c.send().message("Nothing found for '" + msg + "'");
		}
		
	}
	
	private void quit(final User u, final Channel c) {
		
		if (isAuthorized(u, c)) {
			
			c.send().message("Understood, quitting.");
			bot.stopBotReconnect();
			bot.sendIRC().quitServer("Requested");
			
		}
		
	}
	
	private void quit(final User u) {
		u.send().message("Understood, quitting.");
		bot.stopBotReconnect();
		bot.sendIRC().quitServer("Requested");
	}
	
	private void update(final User u, final Channel c) {
		if (isAuthorized(u, c)) {
			c.send().message("Understood, updating.");
			xmlFile.update();
		}
	}
	
	private void update(final User u) {
		u.send().message("Understood, updating.");
		xmlFile.update();
	}
	
	private boolean isAuthorized(final User u, final Channel c) {
		
		return c.isOwner(u) || c.isSuperOp(u)
				|| c.isOp(u) || c.isHalfOp(u);
		
	}
	
	private void searchXml(final Channel c, final String msg) {
		
		final String[] data = xmlFile.getData(msg);
		
		if (data != null) {
			sendXml(c, data);
		}
		
	}
	
}
