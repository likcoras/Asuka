package io.github.likcoras.ssbot.data;

import java.util.Date;

public class SilentLatestData implements SeriesData {
	
	private static final String FORMAT = DataUtils.addBold("%bLatest: %b%s (%s hours ago) %b%s%b");
	
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
	
	public void setDate(Date date) {
		
		this.date = date;
		
	}
	
	public void setLink(String link) {
		
		this.link = link;
		
	}
	
	@Override
	public String ircString() {
		
		if (title.isEmpty() || date == null || link.isEmpty())
			throw new IllegalStateException("Every field must be set in SilentLatestData");
		
		return String.format(FORMAT, title, DataUtils.representDateAs(date, DataUtils.HOURS), link);
		
	}
	
}
