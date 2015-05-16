package io.github.likcoras.ssbot.core;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.util.BotUtils;

import org.apache.log4j.Logger;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class QuitCoreHandler {
	
	private static final Logger LOG = Logger.getLogger(QuitCoreHandler.class);
	
	private final String quitMsg;
	
	public QuitCoreHandler(final ConfigParser cfg) {
		
		quitMsg = cfg.getProperty("quitmsg");
		
	}
	
	public BotCoreResult quit(final User user, final Channel chan) {
		
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
	
}
