package io.github.likcoras.ssbot.backends;

import java.util.regex.Pattern;

public class HandlerUtils {
	
	public static boolean checkHandler(final String query,
		final Pattern... patterns) {
		
		for (final Pattern pattern : patterns)
			return pattern.matcher(query).find();
		
		return false;
		
	}
	
}
