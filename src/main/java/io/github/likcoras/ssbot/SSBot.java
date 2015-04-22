package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.DbHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;

public class SSBot {
	
	public static void main(final String[] args) {
		
		final ConfigParser cfg = new ConfigParser();
		
		System.out.println("Loading database backend...");
		final DbHandler db = new DbHandler(cfg);
		
		System.out.println("Loading mangaupdates backend...");
		final MuHandler mu = new MuHandler(cfg);
		
		System.out
				.println("Loading xml backend...(Fetching the xml file from another thread)");
		final XmlHandler xml = new XmlHandler(cfg);
		
		System.out.println("Loading batoto backend...");
		final BttHandler bt = new BttHandler(cfg);
		
		System.out.println("Starting bot...");
		new BotManager(cfg, db, mu, xml, bt);
		
		System.out.println("Bye!");
		
	}
	
}
