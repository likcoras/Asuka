package io.github.likcoras.ssbot.data;

import io.github.likcoras.ssbot.util.BotUtils;
import io.github.likcoras.ssbot.util.TimeDiff;

import java.util.Date;

public class SilentLatestData implements SeriesData {
	
	private static final String FORMAT = BotUtils
		.addBold("%bLatest: %b%s (%s ago) %b%s%b");
	
	private String title;
	private Date date;
	private String link;
	
	public SilentLatestData() {
		
		title = "";
		date = null;
		link = "";
		
	}
	
	public void setTitle(final String name) {
		
		title = name;
		
	}
	
	public void setDate(final Date date) {
		
		this.date = date;
		
	}
	
	public void setLink(final String link) {
		
		this.link = link;
		
	}
	
	@Override
	public String ircString() {
		
		if (title.isEmpty() || date == null || link.isEmpty())
			throw new IllegalStateException(
				"Every field must be set in SilentLatestData");
		
		String time = TimeDiff.getTime(System.currentTimeMillis() - date.getTime()).getSimpleMessage();
		
		return String.format(FORMAT, title, time, link);
		
	}
	
}
