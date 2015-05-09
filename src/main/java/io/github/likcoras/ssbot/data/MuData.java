package io.github.likcoras.ssbot.data;

import io.github.likcoras.ssbot.data.values.Release;

import java.util.ArrayList;
import java.util.List;

public class MuData implements SeriesData {
	
	private static final String FORMAT =
		DataUtils
			.addBold("%bName: %b%s | %bAuthor: %b%s | %bTags: %b%s | %bLatest Release: %b%s | %bLink: %b%s");
	
	private String title;
	private String author;
	private final List<String> tags;
	private Release latest;
	private String link;
	
	public MuData() {
		
		title = "";
		author = "";
		tags = new ArrayList<String>();
		latest = null;
		link = "";
		
	}
	
	public void setTitle(final String title) {
		
		this.title = title;
		
	}
	
	public void setAuthor(final String author) {
		
		this.author = author;
		
	}
	
	public void addTag(final String tag) {
		
		tags.add(tag);
		
	}
	
	public void setLatest(final Release latest) {
		
		this.latest = latest;
		
	}
	
	public void setLink(final String link) {
		
		this.link = link;
		
	}
	
	@Override
	public String ircString() {
		
		if (title.isEmpty() || author.isEmpty() || tags.isEmpty()
			|| latest == null || link.isEmpty())
			throw new IllegalStateException("Every field must be set in MuData");
		
		return String.format(FORMAT, title, author, DataUtils.tagList(tags),
			DataUtils.releaseText(latest), link);
		
	}
	
	@Override
	public String toString() {
		
		return link;
		
	}
	
}
