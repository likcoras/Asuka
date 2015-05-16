package io.github.likcoras.ssbot.core;

import io.github.likcoras.ssbot.ignore.IgnoreHandler;
import io.github.likcoras.ssbot.util.BotUtils;

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class IgnoreCoreHandler {
	
	private static final Logger LOG = Logger.getLogger(IgnoreCoreHandler.class);
	
	private static final String[] IGNORE_HELP =
		{
				BotUtils.addBold("%b.ignore usage%b"),
				BotUtils
					.addBold("%b.ignore add [nick]:%b makes the bot ignore the user"),
				BotUtils
					.addBold("%b.ignore rem [nick]:%b removes the ignore from the user"),
				BotUtils.addBold("%b.ignore list:%b lists the ignored users") };
	
	private final IgnoreHandler ignore;
	
	public IgnoreCoreHandler() throws IOException {
		
		ignore = new IgnoreHandler();
		ignore.loadIgnores();
		
	}
	
	public boolean checkIgnore(final User user) {
		
		return ignore.isIgnored(user.getNick());
		
	}
	
	public void ignore(final User user, final Channel chan, final String msg) {
		
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
		
		final StringBuilder ignoreBuild = new StringBuilder();
		for (final String ignore : ignoreList)
			ignoreBuild.append(ignore).append(", ");
		
		final String ignores =
			ignoreBuild.substring(0, ignoreBuild.lastIndexOf(","));
		user.send().notice("Ignored Users: " + ignores);
		
	}
	
}
