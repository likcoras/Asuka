package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.data.SeriesData;

public interface DataHandler {
	
	public boolean isHandlerOf(String query);
	
	public SeriesData getData(String query) throws NoResultsException;
	
}
