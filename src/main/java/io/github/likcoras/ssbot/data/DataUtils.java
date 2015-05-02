package io.github.likcoras.ssbot.data;

import java.util.List;

import org.pircbotx.Colors;

public class DataUtils {
	
	private static final String RELEASE_FORMAT = "%s by %s (%s days ago)";
	
	public static String tagList(final List<String> tags) {
		
		String out = "";
		for (final String tag : tags)
			out += tag + ", ";
		
		return out.substring(0, out.length() - 2);
		
	}
	
	public static String releaseText(final Release release) {
		
		final String days =
			Long.toString((System.currentTimeMillis() - release.getDate()
				.getTime()) / 86400000);
		
		return String.format(RELEASE_FORMAT, release.getChapter(),
			release.getGroup(), days);
		
	}
	
	public static String addBold(final String format) {
		
		return format.replaceAll("%b", Colors.BOLD);
		
	}
	
}
