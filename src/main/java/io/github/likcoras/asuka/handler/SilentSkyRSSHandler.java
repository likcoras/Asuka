package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.SilentSkyRSSResponse;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpClientFeedFetcher;
import com.rometools.fetcher.impl.LinkedHashMapFeedInfoCache;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;

public class SilentSkyRSSHandler extends TranslatingHandler {

	private static final String RSS_LINK = "http://www.silentsky-scans.net/feed/";

	private FeedFetcher fetcher = new HttpClientFeedFetcher(LinkedHashMapFeedInfoCache.getInstance());

	@Override
	public BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event) throws HandlerException {
		if (!BotUtil.isTrigger(event.getMessage(), "latest"))
			return EmptyResponse.get();
		try {
			return onHandle(bot, event);
		} catch (FetcherException | FeedException | IOException e) {
			throw new HandlerException(this, "Exception while fetching data", e);
		}
	}

	@Override
	public BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event) throws HandlerException {
		if (!BotUtil.isTrigger(event.getMessage(), "latest"))
			return EmptyResponse.get();
		try {
			return onHandle(bot, event);
		} catch (FetcherException | FeedException | IOException e) {
			throw new HandlerException(this, "Exception while fetching data", e);
		}
	}

	private BotResponse onHandle(AsukaBot bot, GenericMessageEvent<PircBotX> event)
			throws FetcherException, FeedException, IOException {
		List<SyndEntry> entries = fetcher.retrieveFeed(new URL(RSS_LINK)).getEntries();
		if (entries.isEmpty())
			return EmptyResponse.get();
		SyndEntry latest = entries.get(0);
		String title = latest.getTitle();
		String date = DateFormat.getDateTimeInstance().format(latest.getPublishedDate());
		String link = latest.getLink();
		return new SilentSkyRSSResponse(event, title, date, link);
	}

}
