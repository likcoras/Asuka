package io.github.likcoras.ssbot;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.pircbotx.exception.IrcException;
import org.xml.sax.SAXException;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;

public class SSBot {
	
	public static void main(final String[] args) {
		
		final ConfigParser cfg = new ConfigParser();
		
		try {
			
			cfg.parse();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		BotManager bot = new BotManager(cfg);
		bot.registerHandler(new BttHandler(cfg));
		
		try {
			
			bot.registerHandler(new MuHandler(cfg));
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
		XmlHandler xml = new XmlHandler(cfg);
		
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
