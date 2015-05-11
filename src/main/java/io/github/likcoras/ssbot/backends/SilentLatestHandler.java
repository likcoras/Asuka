package io.github.likcoras.ssbot.backends;

import java.io.IOException;
import java.net.URL;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpClientFeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.backends.exceptions.InvalidHandlerException;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;
import io.github.likcoras.ssbot.data.SeriesData;
import io.github.likcoras.ssbot.data.SilentLatestData;

public class SilentLatestHandler implements DataHandler {
	
	private final String link;
	
	private final FeedFetcher cachedFetcher;
	
	public SilentLatestHandler(final ConfigParser cfg) {
		
		link = cfg.getProperty("latestfile");
		cachedFetcher = new HttpClientFeedFetcher(HashMapFeedInfoCache.getInstance());
		
	}
	
	@Override
	public boolean isHandlerOf(String query) {
		
		return query.equalsIgnoreCase(".latest");
		
	}
	
	@Override
	public SilentLatestData getData(String query) throws NoResultsException {
		
		if (!isHandlerOf(query))
			throw new InvalidHandlerException(query);
		
		try {
			
			SyndFeed feed = cachedFetcher.retrieveFeed(new URL(link));
			
			feed.getEntries();
			
		} catch (FetcherException | IOException | FeedException e) {
			
			throw new NoResultsException(query, e);
			
		}
		
	}
	
}
