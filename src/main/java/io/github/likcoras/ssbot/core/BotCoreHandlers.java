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
	
	private final AuthHandler auth;
	private final QuitCoreHandler quit;
	private final IgnoreCoreHandler ignore;
	private final UptimeCoreHandler uptime;
	
	public BotCoreHandlers(final ConfigParser cfg) throws IOException {
		
		auth = new AuthHandler();
		quit = new QuitCoreHandler(cfg);
		ignore = new IgnoreCoreHandler();
		uptime = new UptimeCoreHandler();
		
	}
	
	public AuthHandler getAuth() {
		
		return auth;
		
	}
	
	public BotCoreResult handle(final User user, final Channel chan,
		final String msg) {
		
		if (isTrigger("ignore", msg)) {
			
			if (auth.checkAuth(UserLevel.OP, user, chan))
				ignore.ignore(user, chan, msg);
			else
				noPerms(user, chan, msg);
			
			return BotCoreResult.IGNORE;
			
		} else if (isTrigger("quit " + user.getBot().getNick(), msg)) {
			
			if (auth.checkAuth(UserLevel.OP, user, chan))
				return quit.quit(user, chan);
			
			noPerms(user, chan, msg);
			return BotCoreResult.IGNORE;
			
		} else if (isTrigger("uptime", msg)) {
			
			uptime.uptime(chan);
			return BotCoreResult.IGNORE;
			
		} else if (ignore.checkIgnore(user))
			return BotCoreResult.IGNORE;
		
		return BotCoreResult.HANDLE;
		
	}
	
	private boolean isTrigger(final String trigger, final String msg) {
		
		return (msg.startsWith(".") || msg.startsWith("!"))
			&& msg.substring(1).startsWith(trigger);
		
	}
	
	private void noPerms(final User user, final Channel chan, final String msg) {
		
		LOG.info("Failed command " + BotUtils.userIdentifier(user));
		chan.send().message(user, "Sorry, you're not allowed to do that!");
		
	}
	
}
