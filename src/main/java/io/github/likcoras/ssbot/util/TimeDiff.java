package io.github.likcoras.ssbot.util;

public class TimeDiff {
	
	private static final long SECOND = 1000L;
	private static final long MINUTE = 60L;
	private static final long HOUR = MINUTE;
	private static final long DAY = 24L;
	private static final long WEEK = 7L;
	
	long seconds;
	long minutes;
	long hours;
	long days;
	long weeks;
	
	public static TimeDiff getTime(final long millis) {
		
		return new TimeDiff(millis);
		
	}
	
	private TimeDiff(final long millis) {
		
		seconds = millis / SECOND;
		minutes = seconds / MINUTE;
		hours = minutes / HOUR;
		days = hours / DAY;
		weeks = days / WEEK;
		
		days = days % WEEK;
		hours = hours % DAY;
		minutes = minutes % HOUR;
		seconds = seconds % MINUTE;
		
	}
	
	public String getSimpleMessage() {
		
		String out;
		if (!(out = getSub(weeks, days, "weeks", "days")).isEmpty()
			|| !(out = getSub(days, hours, "days", "hours")).isEmpty()
			|| !(out = getSub(hours, minutes, "hours", "minutes")).isEmpty()
			|| !(out = getSub(minutes, seconds, "minutes", "seconds"))
				.isEmpty()
			|| !(out = getSub(seconds, 0, "seconds", null)).isEmpty())
			return out;
		
		return "now";
		
	}
	
	private String getSub(final long high, final long low,
		final String highName, final String lowName) {
		
		if (high < 1)
			return "";
		
		String out = high + " " + highName;
		
		if (low > 0)
			out += " " + low + " " + lowName;
		
		return out;
		
	}
	
	public String getComplexMessage() {
		
		String out = "";
		
		if (weeks > 0)
			out += weeks + " weeks ";
		if (days > 0)
			out += days + " days ";
		if (hours > 0)
			out += hours + " hours ";
		if (minutes > 0)
			out += minutes + " minutes ";
		if (seconds > 0)
			out += seconds + " seconds ";
		
		return out.isEmpty() ? "now" : out.substring(0, out.length() - 1);
		
	}
	
}
