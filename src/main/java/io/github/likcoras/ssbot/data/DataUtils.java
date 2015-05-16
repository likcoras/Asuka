package io.github.likcoras.ssbot.data;

import java.util.List;

public class DataUtils {
	
	public static String tagList(final List<String> tags) {
		
		final StringBuilder out = new StringBuilder();
		for (final String tag : tags)
			out.append(tag + ", ");
		
		return out.substring(0, out.lastIndexOf(","));
		
	}
	
}
