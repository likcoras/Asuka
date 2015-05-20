package io.github.likcoras.ssbot.core;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.auth.AuthHandler;
import io.github.likcoras.ssbot.util.BotUtils;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserLevel;

public class BotCoreHandlers {
	
	private static final Logger LOG = Logger.getLogger("Handler");
	
	private final AuthHandler authHandler;
	private final QuitCoreHandler quitHandler;
	private final IgnoreCoreHandler ignoreHandler;
	private final UptimeCoreHandler uptimeHandler;
	
	public BotCoreHandlers(final ConfigParser cfg) throws IOException {
		
		authHandler = new AuthHandler();
		quitHandler = new QuitCoreHandler(cfg);
		ignoreHandler = new IgnoreCoreHandler();
		uptimeHandler = new UptimeCoreHandler();
		
	}
	
	public AuthHandler getAuth() {
		
		return authHandler;
		
	}
	
	public BotCoreResult handle(final User user, final Channel chan,
		final String msg) {
		
		if (isTrigger("ignore", msg)) {
			
			if (authHandler.checkAuth(UserLevel.OP, user, chan))
				ignoreHandler.ignore(user, chan, msg);
			else
				noPerms(user, chan, msg);
			
			return BotCoreResult.IGNORE;
			
		} else if (isTrigger("quit " + user.getBot().getNick(), msg)) {
			
			if (authHandler.checkAuth(UserLevel.OP, user, chan))
				return quitHandler.quit(user, chan);
			
			noPerms(user, chan, msg);
			return BotCoreResult.IGNORE;
			
		} else if (isTrigger("uptime", msg)) {
			
			uptimeHandler.uptime(chan);
			return BotCoreResult.IGNORE;
			
		} else if (ignoreHandler.checkIgnore(user))
			return BotCoreResult.IGNORE;
		
		return BotCoreResult.HANDLE;
		
	}
	
	private boolean isTrigger(final String trigger, final String msg) {
		
		return (msg.startsWith(".") || msg.startsWith("!"))
			&& msg.substring(1).equalsIgnoreCase(trigger);
		
	}
	
	private void noPerms(final User user, final Channel chan, final String msg) {
		
		LOG.info("Failed command " + BotUtils.userIdentifier(user));
		chan.send().message(user, "Sorry, you're not allowed to do that!");
		
	}
	
}
