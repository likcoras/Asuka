package io.github.likcoras.ssbot.backends;

import java.util.regex.Pattern;

public class HandlerUtils {
	
	public static boolean checkHandler(String query, Pattern... patterns) {
		
		for (Pattern pattern : patterns)
			return pattern.matcher(query).find();
		
		return false;
		
	}
	
}
