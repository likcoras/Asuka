package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlHandler {
	
	private final String link;
	private Map<String, String[]> data;
	
	public XmlHandler(final ConfigParser cfg) {
		
		link = cfg.getProperty("xmlfile");
		
	}
	
	public synchronized String[] getData(final String tri) {
		
		return data.get(tri);
		
	}
	
	public void scheduleUpdate() {
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				update();
				
			}
			
		}, 0L, 1000 * 60 * 60 * 24);
		
	}
	
	public synchronized void update() {
		
		try {
			
			final DocumentBuilder build =
					DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document doc = build.parse(link);
			
			data = new HashMap<String, String[]>();
			Node trigger = doc.getFirstChild().getFirstChild();
			
			while (trigger != null) {
				
				final String[] links = new String[5];
				
				Node sub = trigger.getFirstChild().getNextSibling();
				for (int i = 0; i < links.length; i++) {
					
					links[i] = sub.getTextContent();
					sub = sub.getNextSibling();
					
				}
				
				data.put(trigger.getNodeName(), links);
				
				trigger = trigger.getNextSibling();
				
			}
			
		} catch (final Exception e) {
			
			System.out.println("Error: Exception while handling xml file!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
	}
	
}
