package io.github.likcoras.ssbot.backends;

public class InvalidHandlerException extends RuntimeException {
	
	private static final long serialVersionUID = -5771390606430224312L;
	
	private static final String MESSAGE = "Invalid handler for ";
	
	public InvalidHandlerException(String query) {
		
		super(MESSAGE + "'" + query + "'");
		
	}
	
	public InvalidHandlerException(String query, Throwable cause) {
		
		super(MESSAGE + "'" + query + "'", cause);
		
	}
	
}
