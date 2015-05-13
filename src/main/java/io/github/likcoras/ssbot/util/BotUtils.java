package io.github.likcoras.ssbot.util;

import org.pircbotx.Colors;
import org.pircbotx.User;

public class BotUtils {
	
	public static String userIdentifier(User user) {
		
		return user.getNick() + "!" + user.getLogin() + "@"
			+ user.getHostmask();
		
	}
	
	public static String addBold(final String format) {
		
		return format.replaceAll("%b", Colors.BOLD);
		
	}
	
}
