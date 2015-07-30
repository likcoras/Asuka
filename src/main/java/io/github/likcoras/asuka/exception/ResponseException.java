package io.github.likcoras.asuka.exception;

public class ResponseException extends Exception {

	private static final long serialVersionUID = 6722809778289812536L;

	public ResponseException(String message) {
		super(message);
	}

	public ResponseException(Throwable cause) {
		super(cause);
	}

	public ResponseException(String message, Throwable cause) {
		super(message, cause);
	}

}
