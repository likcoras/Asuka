package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.SilentLatestHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;
import io.github.likcoras.ssbot.bot.BotManager;
import io.github.likcoras.ssbot.core.BotCoreHandlers;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class SSBot {
	
	private static final Logger LOG = Logger.getLogger(SSBot.class);
	
	public static void main(final String[] args) {
		
		LOG.info("Starting SSBot...");
		
		try {
			
			final ConfigParser cfg = new ConfigParser();
			cfg.parse();
			
			final BotCoreHandlers coreHandlers = new BotCoreHandlers(cfg);
			coreHandlers.initialize();
			
			final BotManager bot = new BotManager(cfg, coreHandlers);
			registerHandlers(bot, cfg);
			
			bot.start();
			
		} catch (Exception e) {
			
			LOG.error("Error while executing bot", e);
			
		}
		
		LOG.info("Bot stopped, exiting.");
		System.exit(0);
		
	}
	
	private static void registerHandlers(final BotManager bot,
		final ConfigParser cfg) throws ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
		
		bot.registerHandler(new BttHandler(cfg));
		bot.registerHandler(new SilentLatestHandler(cfg));
		bot.registerHandler(new MuHandler(cfg));
		
		final XmlHandler xml = new XmlHandler(cfg);
		xml.update();
		
		bot.registerHandler(xml);
		
	}
	
}
