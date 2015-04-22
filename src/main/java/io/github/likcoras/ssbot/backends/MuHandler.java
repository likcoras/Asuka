package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MuHandler {
	
	private final String base;
	private final String linkBase =
			"https://www.mangaupdates.com/series.html?id=";
	
	public MuHandler(final ConfigParser cfg) {
		
		base = cfg.getProperty("muurl");
		
	}
	
	public String[] getData(final int id) {
		
		final String[] out = new String[7];
		
		try {
			
			final Document doc = Jsoup.connect(base + id).get();
			
			final Elements title = doc.select("h2");
			out[0] = title.get(1).text();
			
			final Elements info = doc.select("dt");
			out[1] = info.get(0).text();
			
			try {
				
				out[2] = info.get(3).text();
				
			} catch (final IndexOutOfBoundsException e) {
				
				out[2] = info.get(2).text();
				
			}
			
			final Elements last = doc.select("td");
			out[3] = last.get(0).text();
			
			final Date releaseDate =
					new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
							.parse(last.get(1).text());
			final long diff =
					(System.currentTimeMillis() - releaseDate.getTime())
							/ (1000 * 60 * 60 * 24);
			
			out[4] = Long.toString(diff);
			out[5] = last.get(2).text();
			
			out[6] = linkBase + id;
			
		} catch (final Exception e) {
			
			System.out.println("Error: Exception while fetching data!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
		return out;
		
	}
	
}
