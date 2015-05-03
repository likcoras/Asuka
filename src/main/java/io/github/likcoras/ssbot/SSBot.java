package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.pircbotx.exception.IrcException;
import org.xml.sax.SAXException;

public class SSBot {
	
	private static final Logger LOG = Logger.getLogger(SSBot.class);
	
	public static void main(final String[] args) {
		
		LOG.info("Starting SSBot...");
		
		final ConfigParser cfg = new ConfigParser();
		
		try {
			
			cfg.parse();
			
		} catch (final IOException e) {
			
			LOG.fatal("Error while loading config:", e);
			
		}
		
		final BotManager bot = new BotManager(cfg);
		bot.registerHandler(new BttHandler(cfg));
		
		try {
			
			bot.registerHandler(new MuHandler(cfg));
			
		} catch (final ClassNotFoundException e) {
			
			LOG.warn("Error while loading MuHandler:", e);
			
		}
		
		final XmlHandler xml = new XmlHandler(cfg);
		
		try {
			
			xml.update();
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			
			LOG.warn("Error while loading XmlHandler:", e);
			
		}
		
		bot.registerHandler(xml);
		
		try {
			
			bot.start();
			
		} catch (IOException | IrcException e) {
			
			LOG.warn("Error while running IRC bot:", e);
			
		}
		
		LOG.info("Bot stopped, exiting.");
		System.exit(0);
		
	}
	
}
