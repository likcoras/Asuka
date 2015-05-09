package io.github.likcoras.ssbot.data.values;

import java.util.Date;

public class Release {
	
	private final String group;
	private final String chapter;
	private final Date date;
	
	public Release(final String group, final String chapter, final Date date) {
		
		this.group = group;
		this.chapter = chapter;
		this.date = date;
		
	}
	
	public String getGroup() {
		
		return group;
		
	}
	
	public String getChapter() {
		
		return chapter;
		
	}
	
	public Date getDate() {
		
		return date;
		
	}
	
}
