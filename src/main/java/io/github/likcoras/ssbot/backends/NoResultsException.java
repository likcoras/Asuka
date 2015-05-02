package io.github.likcoras.ssbot.backends;

public class NoResultsException extends Exception {
	
	private static final long serialVersionUID = -7612841505196225092L;
	
	private static final String MESSAGE = "No results found for keywords ";
	
	public NoResultsException(final String[] keywords) {
		super(getMessage(keywords));
	}
	
	public NoResultsException(final String[] keywords, final Throwable cause) {
		super(getMessage(keywords), cause);
	}
	
	private static String getMessage(final String[] keywords) {
		
		String message = MESSAGE;
		
		for (final String keyword : keywords)
			message += "'" + keyword + "' ";
		
		return message.substring(0, message.length() - 1);
		
	}
	
}
