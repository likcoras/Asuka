package io.github.likcoras.ssbot.data;

import io.github.likcoras.ssbot.data.values.Status;

import java.util.ArrayList;
import java.util.List;

public class BttData implements SeriesData {
	
	private static final String FORMAT =
		DataUtils
			.addBold("%bName: %b%s | %bAuthor: %b%s | %bTags: %b%s | %bStatus: %b%s | %bLink: %b%s");
	
	private String title;
	private String author;
	private final List<String> tags;
	private Status status;
	private String link;
	
	public BttData() {
		
		title = "";
		author = "";
		tags = new ArrayList<String>();
		status = null;
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
	
	public void setStatus(final Status status) {
		
		this.status = status;
		
	}
	
	public void setLink(final String link) {
		
		this.link = link;
		
	}
	
	@Override
	public String ircString() {
		
		if (title.isEmpty() || author.isEmpty() || tags.isEmpty()
			|| status == null || link.isEmpty())
			throw new IllegalStateException(
				"Every field must be set in BttData");
		
		return String.format(FORMAT, title, author, DataUtils.tagList(tags),
			status.getDesc(), link);
		
	}
	
	@Override
	public String toString() {
		
		return link;
		
	}
	
}
