package io.github.likcoras.ssbot.backends.exceptions;

public class InvalidHandlerException extends RuntimeException {
	
	private static final long serialVersionUID = -5771390606430224312L;
	
	private static final String MESSAGE = "Invalid handler for ";
	
	public InvalidHandlerException(final String query) {
		
		super(MESSAGE + "'" + query + "'");
		
	}
	
	public InvalidHandlerException(final String query, final Throwable cause) {
		
		super(MESSAGE + "'" + query + "'", cause);
		
	}
	
}
