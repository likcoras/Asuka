package io.github.likcoras.asuka.exception;

import lombok.NonNull;

public class ResponseException extends Exception {

	private static final long serialVersionUID = 6722809778289812536L;

	public ResponseException(@NonNull String message) {
		super(message);
	}

	public ResponseException(@NonNull Throwable cause) {
		super(cause);
	}

	public ResponseException(@NonNull String message, @NonNull Throwable cause) {
		super(message, cause);
	}
	
}
