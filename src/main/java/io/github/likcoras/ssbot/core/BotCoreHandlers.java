package io.github.likcoras.ssbot.core;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.auth.AuthHandler;
import io.github.likcoras.ssbot.ignore.IgnoreHandler;
import io.github.likcoras.ssbot.util.BotUtils;
import io.github.likcoras.ssbot.util.TimeDiff;

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;

public class BotCoreHandlers {
	
	private static final Logger LOG = Logger.getLogger(BotCoreHandlers.class);
	
	private static final String[] IGNORE_HELP =
		{
				BotUtils.addBold("%b.ignore usage%b"),
				BotUtils
					.addBold("%b.ignore add [nick]:%b makes the bot ignore the user"),
				BotUtils
					.addBold("%b.ignore rem [nick]:%b removes the ignore from the user"),
				BotUtils.addBold("%b.ignore list:%b lists the ignored users") };
	
	private final String quitMsg;
	
	private final Long startTime;
	
	private final AuthHandler auth;
	private final IgnoreHandler ignore;
	
	public BotCoreHandlers(final ConfigParser cfg) {
		
		quitMsg = cfg.getProperty("quitmsg");
		
		startTime = System.currentTimeMillis();
		
		auth = new AuthHandler();
		ignore = new IgnoreHandler();
		
	}
	
	public void initialize() throws IOException {
		
		ignore.loadIgnores();
		
	}
	
	public AuthHandler getAuth() {
		
		return auth;
		
	}
	
	public BotCoreResult handle(final User user, final Channel chan,
		final String msg) {
		
		if (msg.toLowerCase().startsWith(".ignore")
			|| msg.toLowerCase().startsWith("!ignore")) {
			
			if (auth.checkAuth(UserLevel.OP, user, chan))
				ignore(user, chan, msg);
			else
				failIgnore(user, chan, msg);
			
			return BotCoreResult.IGNORE;
			
		} else if (msg.equalsIgnoreCase(".quit " + user.getBot().getNick())
			|| msg.equalsIgnoreCase("!quit " + user.getBot().getNick())) {
			
			if (auth.checkAuth(UserLevel.OP, user, chan))
				return quit(user, chan);
			else
				return failQuit(user, chan);
			
		} else if (msg.equalsIgnoreCase(".uptime")
			|| msg.equalsIgnoreCase("!uptime")) {
			
			uptime(chan);
			return BotCoreResult.IGNORE;
			
		} else if (ignore.isIgnored(user.getNick()))
			return BotCoreResult.IGNORE;
		
		return BotCoreResult.HANDLE;
		
	}
	
	private void ignore(final User user, final Channel chan, final String msg) {
		
		LOG.info("Ignore command by " + BotUtils.userIdentifier(user) + ": "
			+ msg);
		
		final String[] command = msg.split("\\s+");
		
		try {
			
			if (command.length < 2)
				invalidIgnore(user);
			else if (command[1].equalsIgnoreCase("add"))
				ignoreAdd(chan, command[2]);
			else if (command[1].equalsIgnoreCase("rem"))
				ignoreRem(chan, command[2]);
			else if (command[1].equalsIgnoreCase("list"))
				ignoreList(user);
			else if (command[1].equalsIgnoreCase("help"))
				ignoreUsage(user);
			else
				invalidIgnore(user);
			
		} catch (final IOException e) {
			
			LOG.error("Error while handling ignore command '" + msg + "'", e);
			
		}
		
	}
	
	private void invalidIgnore(final User user) {
		
		user.send().notice("Wrong usage! Try '.ignore help'!");
		
	}
	
	private void ignoreUsage(final User user) {
		
		for (final String help : IGNORE_HELP)
			user.send().notice(help);
		
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
		
		final Set<String> ignoreList = ignore.listIgnores();
		
		if (ignoreList.isEmpty()) {
			
			user.send().notice("There are no ignored users!");
			return;
			
		}
		
		StringBuilder ignoreBuild = new StringBuilder();
		for (final String ignore : ignoreList)
			ignoreBuild.append(ignore).append(", ");
		
		String ignores = ignoreBuild.substring(0, ignoreBuild.lastIndexOf(","));
		user.send().notice("Ignored Users: " + ignores);
		
	}
	
	private void failIgnore(final User user, final Channel chan,
		final String msg) {
		
		LOG.info("Failed ignore attempt by " + BotUtils.userIdentifier(user));
		chan.send().message(user, "Sorry, you're not allowed to do that!");
		
	}
	
	private BotCoreResult quit(final User user, final Channel chan) {
		
		LOG.info("Quit requested by " + BotUtils.userIdentifier(user));
		broadcastQuit(user);
		
		return BotCoreResult.QUIT;
		
	}
	
	private void broadcastQuit(final User user) {
		
		final String msg = String.format(quitMsg, user.getNick());
		
		for (final Channel chan : user.getBot().getUserChannelDao()
			.getAllChannels())
			chan.send().message(msg);
		
	}
	
	private BotCoreResult failQuit(final User user, final Channel chan) {
		
		LOG.info("Failed quit attempt by " + BotUtils.userIdentifier(user));
		chan.send().message(user, "Sorry, you're not allowed to do that!");
		
		return BotCoreResult.IGNORE;
		
	}
	
	private void uptime(final Channel chan) {
		
		final String uptime =
			TimeDiff.getTime(System.currentTimeMillis() - startTime)
				.getComplexMessage();
		chan.send().message("Uptime: " + uptime);
		
	}
	
}
