package io.github.likcoras.ssbot.data;

public class XmlUpdateData extends XmlData {
	
	private static final String MESSAGE = "Re-fetched the xml file";
	
	@Override
	public void setTitle(final String name) {
		
		throw new UnsupportedOperationException(
			"Cannot edit fields in update data");
		
	}
	
	@Override
	public void setChapter(final String chapter) {
		
		throw new UnsupportedOperationException(
			"Cannot edit fields in update data");
		
	}
	
	@Override
	public void setLinkMediafire(final String link) {
		
		throw new UnsupportedOperationException(
			"Cannot edit fields in update data");
		
	}
	
	@Override
	public void setLinkMega(final String link) {
		
		throw new UnsupportedOperationException(
			"Cannot edit fields in update data");
		
	}
	
	@Override
	public void setLinkReader(final String link) {
		
		throw new UnsupportedOperationException(
			"Cannot edit fields in update data");
		
	}
	
	@Override
	public String ircString() {
		
		return MESSAGE;
		
	}
	
}
