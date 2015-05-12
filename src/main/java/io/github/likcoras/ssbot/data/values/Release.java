package io.github.likcoras.ssbot.data.values;

import io.github.likcoras.ssbot.data.DataUtils;

import java.util.Date;

public class Release {
	
	private static final String RELEASE_FORMAT = "%s by %s (%s days ago)";
	
	private final String group;
	private final String chapter;
	private final Date date;
	
	public static String releaseText(final Release release) {
		
		final String days =
			DataUtils.representDateAs(release.getDate(), DataUtils.DAYS);
		
		return String.format(RELEASE_FORMAT, release.getChapter(),
			release.getGroup(), days);
		
	}
	
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
