package io.github.likcoras.asuka.exception;

import lombok.Getter;
import lombok.NonNull;
import io.github.likcoras.asuka.handler.Handler;

public class HandlerException extends Exception {

	private static final long serialVersionUID = 1643461322728381017L;

	@Getter
	private final Handler handler;

	public HandlerException(@NonNull Handler handler, @NonNull String message) {
		super(message);
		this.handler = handler;
	}

	public HandlerException(@NonNull Handler handler, @NonNull Throwable cause) {
		super(cause);
		this.handler = handler;
	}

	public HandlerException(@NonNull Handler handler, @NonNull String message, @NonNull Throwable cause) {
		super(message, cause);
		this.handler = handler;
	}

}
