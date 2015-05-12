package io.github.likcoras.ssbot.data;

import java.util.Date;
import java.util.List;

public class DataUtils {
	
	public static final int DAYS = 86400000;
	public static final int HOURS = 3600000;
	
	public static String tagList(final List<String> tags) {
		
		String out = "";
		for (final String tag : tags)
			out += tag + ", ";
		
		return out.substring(0, out.length() - 2);
		
	}
	
	public static String representDateAs(final Date date, final int time) {
		
		return Long.toString((System.currentTimeMillis() - date.getTime())
			/ time);
		
	}
	
	public static int getHours(final Date date) {
		
		return (int) (System.currentTimeMillis() - date.getTime()) / HOURS;
		
	}
	
}
