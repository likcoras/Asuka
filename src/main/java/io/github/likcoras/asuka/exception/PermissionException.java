package io.github.likcoras.asuka.exception;

import io.github.likcoras.asuka.handler.Handler;
import lombok.Getter;
import lombok.NonNull;

import org.pircbotx.User;

public class PermissionException extends HandlerException {

	private static final long serialVersionUID = 7446627577816566937L;

	@Getter
	private final User user;
	@Getter
	private final String query;

	public PermissionException(@NonNull Handler handler, User user, @NonNull String query) {
		super(handler, "User " + user.getNick() + "!" + user.getLogin() + "@" + user.getHostmask()
				+ " is not allowed to execute query " + query);
		this.user = user;
		this.query = query;
	}

}
