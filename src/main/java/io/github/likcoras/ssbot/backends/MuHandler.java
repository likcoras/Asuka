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
		
		base = cfg.getString("muurl");
		
	}
	
	public String[] getData(final int id) {
		
		final String[] out = new String[7];
		
		try {
			
			final Document doc = Jsoup.connect(base + id).get();
			
			final Elements tit = doc.select("h2");
			out[0] = tit.get(1).text();
			
			final Elements inf = doc.select("dt");
			out[1] = inf.get(0).text();
			
			try {
				
				out[2] = inf.get(3).text();
				
			} catch (final IndexOutOfBoundsException e) {
				
				out[2] = inf.get(2).text();
				
			}
			
			final Elements lat = doc.select("td");
			out[3] = lat.get(0).text();
			
			final Date d =
					new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
							.parse(lat.get(1).text());
			final long diff =
					(System.currentTimeMillis() - d.getTime())
							/ (1000 * 60 * 60 * 24);
			
			out[4] = Long.toString(diff);
			out[5] = lat.get(2).text();
			
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
