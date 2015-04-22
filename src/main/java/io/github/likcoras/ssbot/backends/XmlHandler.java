package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlHandler {
	
	private final String link;
	private Map<String, String[]> data;
	
	public XmlHandler(final ConfigParser cfg) {
		
		link = cfg.getString("xmlfile");
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				update();
				
			}
			
		});
		
	}
	
	public synchronized String[] getData(final String tri) {
		
		return data.get(tri);
		
	}
	
	public synchronized void update() {
		
		try {
			
			final DocumentBuilder build =
					DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document doc = build.parse(link);
			
			data = new HashMap<String, String[]>();
			Node node = doc.getFirstChild().getFirstChild();
			
			while (node != null) {
				
				final String[] dat = new String[5];
				
				Node sub = node.getFirstChild().getNextSibling();
				for (int i = 0; i < dat.length; i++) {
					
					dat[i] = sub.getTextContent();
					sub = sub.getNextSibling();
					
				}
				
				data.put(node.getNodeName(), dat);
				
				node = node.getNextSibling();
				
			}
			
		} catch (final Exception e) {
			
			System.out.println("Error: Exception while handling xml file!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
	}
	
}
