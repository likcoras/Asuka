package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.pircbotx.exception.IrcException;
import org.xml.sax.SAXException;

public class SSBot {
	
	public static void main(final String[] args) {
		
		BasicConfigurator.configure();
		
		final ConfigParser cfg = new ConfigParser();
		
		try {
			
			cfg.parse();
			
		} catch (final IOException e) {
			
			e.printStackTrace();
			
		}
		
		final BotManager bot = new BotManager(cfg);
		bot.registerHandler(new BttHandler(cfg));
		
		try {
			
			bot.registerHandler(new MuHandler(cfg));
			
		} catch (final ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
		final XmlHandler xml = new XmlHandler(cfg);
		
		try {
			
			xml.update();
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			
			e.printStackTrace();
			
		}
		
		bot.registerHandler(xml);
		
		try {
			bot.start();
		} catch (IOException | IrcException e) {
			
			e.printStackTrace();
			
		}
		
		System.exit(0);
		
	}
	
}
