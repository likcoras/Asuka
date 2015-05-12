package io.github.likcoras.ssbot.data;

import io.github.likcoras.ssbot.data.values.Release;

import java.util.Date;
import java.util.List;

import org.pircbotx.Colors;

public class DataUtils {
	
	private static final String RELEASE_FORMAT = "%s by %s (%s days ago)";
	
	public static final int DAYS = 86400000;
	public static final int HOURS = 3600000;
	
	public static String tagList(final List<String> tags) {
		
		String out = "";
		for (final String tag : tags)
			out += tag + ", ";
		
		return out.substring(0, out.length() - 2);
		
	}
	
	public static String representDateAs(Date date, int time) {
		
		return Long.toString((System.currentTimeMillis() - date
			.getTime()) / time);
		
	}
	
	public static String releaseText(final Release release) {
		
		final String days = representDateAs(release.getDate(), DAYS);
		
		return String.format(RELEASE_FORMAT, release.getChapter(),
			release.getGroup(), days);
		
	}
	
	public static String addBold(final String format) {
		
		return format.replaceAll("%b", Colors.BOLD);
		
	}
	
}
