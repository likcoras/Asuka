package io.github.likcoras.ssbot.data;

public class XmlData implements SeriesData {
	
	private static final String FORMAT =
		DataUtils
			.addBold("%b%s%b chapter %b%s%b | %bMediafire: %b%s | %bMega: %b%s | %bReader: %b%s");
	
	private String title;
	private String chapter;
	
	private String mediafire;
	private String mega;
	private String reader;
	
	public XmlData() {
		
		title = "";
		chapter = "";
		mediafire = "";
		mega = "";
		reader = "";
		
	}
	
	public void setTitle(final String name) {
		
		title = name;
		
	}
	
	public void setChapter(final String chapter) {
		
		this.chapter = chapter;
		
	}
	
	public void setLinkMediafire(final String link) {
		
		mediafire = link;
		
	}
	
	public void setLinkMega(final String link) {
		
		mega = link;
		
	}
	
	public void setLinkReader(final String link) {
		
		reader = link;
		
	}
	
	@Override
	public String ircString() {
		
		if (title.isEmpty() || chapter.isEmpty() || mediafire.isEmpty()
			|| mega.isEmpty() || reader.isEmpty())
			throw new IllegalStateException(
				"Every field must be set in XmlData");
		
		return String.format(FORMAT, title, chapter, mediafire, mega, reader);
		
	}
	
	@Override
	public String toString() {
		
		return title + " " + chapter;
		
	}
	
}
