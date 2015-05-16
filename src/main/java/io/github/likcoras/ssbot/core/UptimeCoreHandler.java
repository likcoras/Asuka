package io.github.likcoras.ssbot.core;

import io.github.likcoras.ssbot.util.TimeDiff;

import org.pircbotx.Channel;

public class UptimeCoreHandler {
	
	private final long start;
	
	public UptimeCoreHandler() {
		
		start = System.currentTimeMillis();
		
	}
	
	public void uptime(final Channel chan) {
		
		final String uptime =
			TimeDiff.getTime(System.currentTimeMillis() - start)
				.getComplexMessage();
		chan.send().message("Uptime: " + uptime);
		
	}
	
}
