package io.github.likcoras.ssbot.data;

import java.util.List;

public class DataUtils {
	
	public static String tagList(final List<String> tags) {
		
		String out = "";
		for (final String tag : tags)
			out += tag + ", ";
		
		return out.substring(0, out.length() - 2);
		
	}
	
}
