package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.SilentLatestHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;
import io.github.likcoras.ssbot.bot.BotManager;
import io.github.likcoras.ssbot.core.BotCoreHandlers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.pircbotx.exception.IrcException;
import org.xml.sax.SAXException;

public class SSBot {
	
	private static final Logger LOG = Logger.getLogger(SSBot.class);
	
	public static void main(final String[] args) {
		
		try {
			
			final ConfigParser config = new ConfigParser();
			
			if (args.length > 0)
				config.parse();
			else
				config.parse(new File(args[0]));
			
			final BotCoreHandlers coreHandlers = new BotCoreHandlers(config);
			
			final BotManager bot = new BotManager(config, coreHandlers);
			registerHandlers(bot, config);
			
			bot.start();
			
		} catch (ClassNotFoundException | IOException | SAXException | ParserConfigurationException | IrcException e) {
			
			LOG.error("Error while executing bot", e);
			
		}
		
		LOG.info("Bot stopped, exiting.");
		
	}
	
	private static void registerHandlers(final BotManager bot,
		final ConfigParser config) throws ClassNotFoundException, IOException,
		SAXException, ParserConfigurationException {
		
		bot.registerHandler(new BttHandler(config));
		bot.registerHandler(new SilentLatestHandler(config));
		bot.registerHandler(new MuHandler(config));
		
		final XmlHandler xml = new XmlHandler(config);
		xml.update();
		
		bot.registerHandler(xml);
		
	}
	
}
