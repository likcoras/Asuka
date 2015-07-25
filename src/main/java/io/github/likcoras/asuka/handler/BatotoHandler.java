package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.handler.response.BatotoResponse;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.NoResultResponse;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class BatotoHandler extends TranslatingHandler {

	private static final String SEARCH_URL = "http://bato.to/search?name_cond=c&order_cond=views&order=desc&name=";
	private static final Pattern LINK_PATTERN = Pattern.compile("((https?://)?bato.to/comic/_/(comics/)?\\S+/?)");

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
		if (BotUtil.isTrigger(event.getMessage(), "batoto"))
			return getBatoto(event);
		Matcher matcher = LINK_PATTERN.matcher(event.getMessage());
		if (matcher.find())
			return getBatotoLink(event, matcher.group(1));
		return EmptyResponse.get();
	}

	private BotResponse getBatoto(GenericMessageEvent<PircBotX> event) throws IOException {
		if (event.getMessage().length() < 8)
			return EmptyResponse.get();
		Document document = Jsoup.parse(new URL(SEARCH_URL + BotUtil.urlEncode(event.getMessage().substring(8))),
				10000);
		Elements results = document.select("strong > a[href]");
		if (results.isEmpty())
			return new NoResultResponse(event);
		return getBatotoLink(event, results.get(0).attr("href"));
	}

	private BotResponse getBatotoLink(GenericMessageEvent<PircBotX> event, String link) throws IOException {
		Document document = Jsoup.parse(new URL(link.startsWith("http") ? link : "http://" + link), 10000);
		String title = document.select("h1.ipsType_pagetitle").text();
		String author = document.select("td:contains(Author:) + td").text();
		String genres = document.select("td:contains(Genres:) + td").text();
		String status = document.select("td:contains(Status:) + td").text();
		return new BatotoResponse(event, title, author, genres, status, link);
	}

}
