package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.response.ExceptionResponse;
import io.github.likcoras.asuka.handler.response.SilentSkyRSSResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;

@Log4j2
public class SilentSkyRSSHandler extends Handler {

	private static final String RSS_LINK = "http://www.silentsky-scans.net/feed/";

	private HttpURLFeedFetcher fetcher = new HttpURLFeedFetcher();

	public SilentSkyRSSHandler(AsukaBot bot) {
		super(bot);
		fetcher.setConnectTimeout(10000);
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		if (BotUtil.isTrigger(event.getMessage(), "latest"))
			try {
				onHandle(event);
			} catch (FetcherException | FeedException | IOException e) {
				getBot().send(new ExceptionResponse(this, event, e));
				log.error(BotUtil.getExceptionMessage(this, event, e), e);
			}
	}

	private void onHandle(GenericMessageEvent<PircBotX> event) throws FetcherException, FeedException, IOException {
		List<SyndEntry> entries = fetcher.retrieveFeed(new URL(RSS_LINK)).getEntries();
		if (entries.isEmpty())
			return;
		SyndEntry latest = entries.get(0);
		String title = latest.getTitle();
		Instant date = Instant.ofEpochMilli(latest.getPublishedDate().getTime());
		String link = latest.getLink();
		getBot().send(new SilentSkyRSSResponse(event, title, date, link));
	}

}
