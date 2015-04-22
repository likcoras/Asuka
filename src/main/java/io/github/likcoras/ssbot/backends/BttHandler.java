package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BttHandler {
	
	private final String base;
	
	public BttHandler(final ConfigParser cfg) {
		
		base = cfg.getString("bturl");
		
	}
	
	public String[] getData(String link) {
		
		try {
			
			if (!link.startsWith("http")) {
				
				link = "http://" + link;
				
			}
			
			final Document doc = Jsoup.connect(link).get();
			final String[] data = new String[5];
			
			data[0] = doc.select("h1.ipsType_pagetitle").text();
			
			final Elements inf = doc.select("td > a");
			data[1] = inf.get(0).text();
			
			final Elements gen = doc.select("td > a[href] > span");
			data[2] = "";
			for (final Element a : gen) {
				
				data[2] += a.text() + ", ";
				
			}
			data[2] =
					data[2].substring(0, data[2].length() - 2).replaceAll(",",
							"");
			
			final Elements stat = doc.select("td");
			
			for (final Element a : stat) {
				
				if (a.text().equals("Status:")) {
					
					data[3] = a.nextElementSibling().text();
					
				}
				
			}
			
			data[4] = link;
			
			return data;
			
		} catch (final IOException e) {
			
			System.out.println("Error: Exception while fetching data!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
		return null;
		
	}
	
	public String[] search(final String search) {
		
		try {
			
			final Document doc = Jsoup.connect(base + search).get();
			
			final Elements es = doc.select("strong > a[href]");
			
			if (es.isEmpty()) {
				
				return null;
				
			}
			
			final Element e = es.get(0);
			
			return getData(e.attr("href"));
			
		} catch (final IOException e) {
			
			System.out.println("Error: Exception while fetching data!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
		return null;
		
	}
	
}
