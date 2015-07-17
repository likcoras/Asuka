package io.github.likcoras.asuka.exception;

import io.github.likcoras.asuka.handler.Handler;
import lombok.Getter;

public class NoResponseException extends HandlerException {

	private static final long serialVersionUID = 7008026288382496236L;
	
	@Getter
	private final String query;
	
	public NoResponseException(Handler handler, String query) {
		super(handler, "No results for query " + query);
		this.query = query;
	}
	
}
