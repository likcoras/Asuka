package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.response.BatotoResponse;
import io.github.likcoras.asuka.handler.response.ExceptionResponse;
import io.github.likcoras.asuka.handler.response.NoResultResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

@Log4j2
public class BatotoHandler extends Handler {

	private static final String SEARCH_URL = "http://bato.to/search?name_cond=c&order_cond=views&order=desc&name=";
	private static final Pattern LINK_PATTERN = Pattern.compile("((https?://)?bato.to/comic/_/(comics/)?\\S+/?)");

	public BatotoHandler(AsukaBot bot) throws ConfigException {
		super(bot);
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		try {
			handle(event);
		} catch (IOException e) {
			getBot().send(new ExceptionResponse(this, event, e));
			log.error(BotUtil.getExceptionMessage(this, event, e), e);
		}
	}

	private void handle(GenericMessageEvent<PircBotX> event) throws IOException {
		if (BotUtil.isTrigger(event.getMessage(), "batoto"))
			getBatoto(event);
		Matcher matcher = LINK_PATTERN.matcher(event.getMessage());
		if (matcher.find())
			getBatotoLink(event, matcher.group(1));
	}

	private void getBatoto(GenericMessageEvent<PircBotX> event) throws IOException {
		if (event.getMessage().length() < 8)
			return;
		Document document = Jsoup.parse(new URL(SEARCH_URL + BotUtil.urlEncode(event.getMessage().substring(8))),
				10000);
		Elements results = document.select("strong > a[href]");
		if (results.isEmpty())
			getBot().send(new NoResultResponse(event));
		getBatotoLink(event, results.get(0).attr("href"));
	}

	private void getBatotoLink(GenericMessageEvent<PircBotX> event, String link) throws IOException {
		Document document = Jsoup.parse(new URL(link.startsWith("http") ? link : "http://" + link), 10000);
		String title = document.select("h1.ipsType_pagetitle").text();
		String author = document.select("td:contains(Author:) + td").text();
		String genres = document.select("td:contains(Genres:) + td").text();
		String status = document.select("td:contains(Status:) + td").text();
		getBot().send(new BatotoResponse(event, title, author, genres, status, link));
	}

}
