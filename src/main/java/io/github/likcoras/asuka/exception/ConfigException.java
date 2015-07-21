package io.github.likcoras.asuka.exception;

import lombok.Getter;
import lombok.NonNull;

public class ConfigException extends Exception {

	private static final long serialVersionUID = -1935081893936314776L;

	@Getter
	private final String key;
	@Getter
	private final Reason reason;
	@Getter
	private final Class<?> requiredType;

	public ConfigException(@NonNull String key) {
		super("Attempted to access missing required key " + key);
		this.key = key;
		this.reason = Reason.MISSING;
		this.requiredType = null;
	}

	public ConfigException(@NonNull String key, Class<?> type) {
		super("Attempted to access key " + key + " as " + type.getSimpleName());
		this.key = key;
		this.reason = Reason.INVALID;
		this.requiredType = type;
	}

	public static enum Reason {
		MISSING, INVALID;
	}

}
