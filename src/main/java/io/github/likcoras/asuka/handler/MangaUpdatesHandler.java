package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.MangaUpdatesResponse;
import io.github.likcoras.asuka.handler.response.NoResultResponse;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class MangaUpdatesHandler extends TranslatingHandler {

	private static final String SEARCH_URL = "https://rlstrackr.com/search/series/?q=";
	private static final String DISPLAY_URL = "http://www.mangaupdates.com/series.html?id=";
	private static final String INFO_URL = "https://rlstrackr.com/series/info/";
	private static final Pattern LINK_PATTERN = Pattern.compile(
			"((https?://)?(www\\.)?((rlstrackr.com/series/info/)|(mangaupdates.com/series.html\\?id=))(\\d+)/?)");

	@Override
	public BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event) throws HandlerException {
		try {
			return handle(event);
		} catch (IOException e) {
			throw new HandlerException(this, "Exception while fetching data", e);
		}
	}

	@Override
	public BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event) throws HandlerException {
		try {
			return handle(event);
		} catch (IOException e) {
			throw new HandlerException(this, "Exception while fetching data", e);
		}
	}

	private BotResponse handle(GenericMessageEvent<PircBotX> event) throws IOException {
		if (BotUtil.isTrigger(event.getMessage(), "baka"))
			return getMangaUpdates(event);
		Matcher matcher = LINK_PATTERN.matcher(event.getMessage());
		if (matcher.find())
			return getMangaUpdatesLink(event, INFO_URL + matcher.group(7));
		return EmptyResponse.get();
	}

	private BotResponse getMangaUpdates(GenericMessageEvent<PircBotX> event) throws IOException {
		if (event.getMessage().length() < 7)
			return EmptyResponse.get();
		Document document = Jsoup.parse(new URL(SEARCH_URL + event.getMessage().substring(6)), 10000);
		Elements results = document.select("table.table.no-border a[href]");
		if (results.isEmpty())
			return new NoResultResponse(event);
		return getMangaUpdatesLink(event, results.get(0).attr("abs:href"));
	}

	private BotResponse getMangaUpdatesLink(GenericMessageEvent<PircBotX> event, String link) throws IOException {
		Document document = Jsoup.parse(new URL(link.startsWith("http") ? link : "http://" + link), 10000);
		String title = document.select("div.content header h2").text();
		String author = document.select("dd:contains(Author) + dt").isEmpty() ? "Unknown"
				: document.select("dd:contains(Author) + dt").get(0).text();
		String tags = document.select("dd:contains(Tags) + dt").text();
		Elements releases = document.select("table.table.no-border tr");
		String group;
		LocalDate date;
		String chapter;
		if (releases.isEmpty()) {
			group = "";
			date = LocalDate.MAX;
			chapter = "";
		} else {
			Elements lastRelease = releases.get(0).getAllElements().get(0).getAllElements();
			group = lastRelease.get(2).text();
			date = LocalDate.parse(lastRelease.get(1).text(), DateTimeFormatter.ofPattern("MMMM dd, uuuu", Locale.US));
			chapter = lastRelease.get(0).text();
		}
		Matcher matcher = LINK_PATTERN.matcher(link);
		matcher.find();
		link = DISPLAY_URL + matcher.group(7);
		return new MangaUpdatesResponse(event, new MangaUpdatesData(title, author, tags, group, date, chapter, link));
	}

	@Value
	public static class MangaUpdatesData {
		@NonNull
		String title;
		@NonNull
		String author;
		@NonNull
		String tags;
		@NonNull
		String group;
		@NonNull
		LocalDate date;
		@NonNull
		String chapter;
		@NonNull
		String link;
	}

}
