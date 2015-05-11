package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.auth.AuthHandler;
import io.github.likcoras.ssbot.auth.AuthListener;
import io.github.likcoras.ssbot.backends.DataHandler;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;
import io.github.likcoras.ssbot.ignore.IgnoreHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class BotManager extends ListenerAdapter<PircBotX> {
	
	private static final Logger LOG = Logger.getLogger(BotManager.class);
	private static final Logger HANDLE = Logger.getLogger("Handler");
	
	private final PircBotX bot;
	private final List<DataHandler> handlers;
	
	private final AuthHandler auth;
	private final IgnoreHandler ignore;
	
	public BotManager(final ConfigParser cfg) {
		
		handlers = new ArrayList<DataHandler>();
		
		auth = new AuthHandler();
		ignore = new IgnoreHandler();
		
		bot = new PircBotX(configure(cfg));
		
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> eve) {
		
		final String msg = eve.getMessage();
		final User user = eve.getUser();
		final Channel chan = eve.getChannel();
		
		if (msg.toLowerCase().startsWith(".ignore")) {
			
			if (auth.checkAuth(UserLevel.OP, user, chan))
				ignore(user, chan, msg);
			else
				failIgnore(user, chan, msg);
			
			return;
			
		}
		
		if (msg.equalsIgnoreCase(".quit")) {
			
			if (auth.checkAuth(UserLevel.OP, user, chan))
				quit(user, chan);
			else
				failQuit(user, chan);
			
			return;
			
		}
		
		if (ignore.isIgnored(user.getNick()))
			return;
		
		doHandlers(user, chan, msg);
		
	}
	
	public void registerHandler(final DataHandler handler) {
		
		synchronized (handlers) {
			
			handlers.add(handler);
			
		}
		
	}
	
	public void start() throws IOException, IrcException {
		
		initialize();
		
		LOG.info("Starting IRC bot...");
		bot.startBot();
		
	}
	
	private Configuration<PircBotX> configure(final ConfigParser cfg) {
		
		final Builder<PircBotX> build = new Configuration.Builder<PircBotX>();
		
		build
			.addListener(new BotLogger())
			.addListener(new AuthListener(auth))
			.addListener(this)
			.setAutoReconnect(true)
			.setName(cfg.getProperty("ircnick"))
			.setLogin(cfg.getProperty("irclogin"))
			.setRealName(cfg.getProperty("ircrealname"))
			.setServer(cfg.getProperty("irchost"),
				Integer.parseInt(cfg.getProperty("ircport")));
		
		if (Boolean.parseBoolean(cfg.getProperty("ircssl")))
			build.setSocketFactory(new UtilSSLSocketFactory()
				.trustAllCertificates());
		
		if (!cfg.getProperty("ircnickserv").isEmpty())
			build.setNickservPassword(cfg.getProperty("ircnickserv"));
		
		if (!cfg.getProperty("ircpass").isEmpty())
			build.setNickservPassword(cfg.getProperty("ircpass"));
		
		final String[] chans = cfg.getProperty("ircchannels").split(",");
		for (final String chan : chans) {
			
			final String[] chanKey = chan.split(":");
			
			if (chanKey.length > 1)
				build.addAutoJoinChannel(chanKey[0], chanKey[1]);
			else
				build.addAutoJoinChannel(chan);
			
		}
		
		return build.buildConfiguration();
		
	}
	
	private void initialize() {
		
		try {
			
			ignore.loadIgnores();
			
		} catch (final IOException e) {
			
			LOG.error("Error while loading ignores: ", e);
			
		}
		
	}
	
	private String userIdentifier(final User user) {
		
		return user.getNick() + "!" + user.getLogin() + "@"
			+ user.getHostmask();
		
	}
	
	private void ignore(final User user, final Channel chan, final String msg) {
		
		LOG.info("Ignore command by " + userIdentifier(user) + ": " + msg);
		
		final String[] command = msg.split("\\s+");
		
		try {
			
			if (command.length < 2)
				ignoreUsage(user);
			else if (command[1].equalsIgnoreCase("add"))
				ignoreAdd(chan, command[2]);
			else if (command[1].equalsIgnoreCase("rem"))
				ignoreRem(chan, command[2]);
			else if (command[1].equalsIgnoreCase("list"))
				ignoreList(user);
			else
				ignoreUsage(user);
			
		} catch (final IOException e) {
			
			LOG.error("Error while handling ignore command '" + msg + "'", e);
			
		}
		
	}
	
	private void ignoreUsage(final User user) {
		
		user.send().notice(Colors.BOLD + ".ignore usage:");
		user.send().notice(
			Colors.BOLD + ".ignore add [nick]:" + Colors.BOLD
				+ " makes the bot ignore the user");
		user.send().notice(
			Colors.BOLD + ".ignore rem [nick]:" + Colors.BOLD
				+ " removes the ignore from the user");
		user.send().notice(
			Colors.BOLD + ".ignore list:" + Colors.BOLD
				+ " lists the ignored users");
		
	}
	
	private void ignoreAdd(final Channel chan, final String target)
		throws IOException {
		
		ignore.addIgnore(target);
		chan.send().message("User " + target + " was added to the ignore list");
		
	}
	
	private void ignoreRem(final Channel chan, final String target)
		throws IOException {
		
		ignore.delIgnore(target);
		chan.send().message("User " + target + " removed from ignore list");
		
	}
	
	private void ignoreList(final User user) {
		
		String ignores = "";
		for (final String ignore : ignore.listIgnores())
			ignores += ignore + ", ";
		
		ignores = ignores.substring(0, ignores.length() - 2);
		user.send().notice("Ignored Users: " + ignores);
		
	}
	
	private void failIgnore(final User user, final Channel chan,
		final String msg) {
		
		LOG.info("Failed ignore attempt by " + userIdentifier(user) + " in  "
			+ chan.getName() + ": " + msg);
		chan.send().message(user, "Sorry, you're not allowed to do that!");
		
	}
	
	private void quit(final User user, final Channel chan) {
		
		LOG.info("Quit requested by " + userIdentifier(user));
		
		chan.send().message("Understood, quitting.");
		bot.stopBotReconnect();
		bot.sendIRC().quitServer("Requested");
		
	}
	
	private void failQuit(final User user, final Channel chan) {
		
		LOG.info("Failed quit attempt by " + userIdentifier(user) + " in  "
			+ chan.getName());
		chan.send().message(user, "Sorry, you're not allowed to do that!");
		
	}
	
	private void doHandlers(final User user, final Channel chan,
		final String msg) {
		
		for (final DataHandler handler : handlers)
			if (handler.isHandlerOf(msg))
				try {
					
					HANDLE.info("Handling query '" + msg + "' by "
						+ userIdentifier(user) + " with handler "
						+ handler.getClass().getSimpleName());
					
					if (!auth.checkAuth(handler, msg, user, chan)) {
						
						HANDLE
							.info("User did not have a high enough access level for executing the query '"
								+ msg + "'");
						chan.send().message(user,
							"Sorry, you're not allowed to do that!");
						return;
						
					}
					
					chan.send().message(handler.getData(msg).ircString());
					
				} catch (final NoResultsException e) {
					
					if (e.getCause() != null) {
						
						HANDLE.error("Error while handing query: ",
							e.getCause());
						chan.send().message(
							"Error(" + e.getCause().getClass().getSimpleName()
								+ "): " + e.getCause().getMessage());
						
					} else
						chan.send().message("No results found");
					
				}
		
	}
	
}
