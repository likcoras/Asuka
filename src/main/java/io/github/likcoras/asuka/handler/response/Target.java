package io.github.likcoras.asuka.handler.response;

import lombok.Getter;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class Target {
	
	@Getter
	private final User user;
	@Getter
	private final Channel channel;
	
	public Target(User user) {
		this.user = user;
		channel = null;
	}
	
	public Target(Channel channel) {
		user = null;
		this.channel = channel;
	}
	
	public boolean isUser() {
		return user != null;
	}
	
}
