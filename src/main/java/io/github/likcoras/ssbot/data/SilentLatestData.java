package io.github.likcoras.ssbot.data;

import io.github.likcoras.ssbot.util.BotUtils;

import java.util.Date;

public class SilentLatestData implements SeriesData {
	
	private static final String FORMAT = BotUtils
		.addBold("%bLatest: %b%s (%s hours ago) %b%s%b");
	
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
		
		String time;
		if (DataUtils.getHours(date) > 100)
			time = DataUtils.representDateAs(date, DataUtils.DAYS);
		else
			time = DataUtils.representDateAs(date, DataUtils.HOURS);
		
		return String.format(FORMAT, title, time, link);
		
	}
	
}
