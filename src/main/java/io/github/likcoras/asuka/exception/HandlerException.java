package io.github.likcoras.asuka.exception;

import lombok.Getter;
import io.github.likcoras.asuka.handler.Handler;

public class HandlerException extends Exception {
	
	private static final long serialVersionUID = 1643461322728381017L;
	
	@Getter
	private final Handler handler;
	
	public HandlerException(Handler handler, String message) {
		super(message);
		this.handler = handler;
	}
	
	public HandlerException(Handler handler, Throwable cause) {
		super(cause);
		this.handler = handler;
	}
	
	public HandlerException(Handler handler, String message, Throwable cause) {
		super(message, cause);
		this.handler = handler;
	}
	
}
