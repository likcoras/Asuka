package io.github.likcoras.ssbot.data;

import java.util.Date;

public class SilentLatestData implements SeriesData {
	
	private static final String FORMAT = DataUtils.addBold("%bLatest: %b%s (%s days ago) %s");
	
	private String title;
	private String description;
	private Date date;
	
	public SilentLatestData() {
		
		title = "";
		description = "";
		date = null;
		
	}
	
	public void setTitle(final String name) {
		
		title = name;
		
	}
	
	public void setDescription(final String description) {
		
		this.description = description;
		
	}
	
	public void setDate(Date date) {
		
		this.date = date;
		
	}
	
	@Override
	public String ircString() {
		
		if (title.isEmpty() || description.isEmpty() || date == null)
			throw new IllegalStateException("Every field must be set in SilentLatestData");
		
		return String.format(FORMAT, title, DataUtils.representDateAs(date, DataUtils.DAYS), description);
		
	}
	
}
