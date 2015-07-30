package io.github.likcoras.asuka.handler.response;

import java.time.LocalDate;
import java.time.Period;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.handler.MangaUpdatesHandler.MangaUpdatesData;

public class MangaUpdatesResponse implements BotResponse {

	private static final String FORMAT = BotUtil
			.addFormat("&bName:&b %s | &bAuthor:&b %s | &bTags:&b %s | &bLast Release:&b %s | &bLink:&b %s");
	private static final String RELEASE_FORMAT = "%s by %s (%s ago)";

	private GenericMessageEvent<PircBotX> event;
	private String message;

	public MangaUpdatesResponse(GenericMessageEvent<PircBotX> event, MangaUpdatesData data) {
		this.event = event;
		message = String.format(FORMAT, data.getTitle(), data.getAuthor(), data.getTags(), getReleaseMessage(data),
				data.getLink());
	}

	@Override
	public void send(AsukaBot bot) {
		if (event instanceof MessageEvent)
			((MessageEvent<PircBotX>) event).getChannel().send().message(message);
		else
			event.respond(message);
	}

	private String getReleaseMessage(MangaUpdatesData data) {
		String group = data.getGroup();
		String chapter = data.getChapter();
		LocalDate date = data.getDate();
		if (date.equals(LocalDate.MAX))
			return "No releases";
		Period diff = Period.between(date, LocalDate.now());
		if (diff.isNegative())
			diff = Period.ZERO;
		return String.format(RELEASE_FORMAT, chapter.isEmpty() ? "Unknown chapter" : chapter,
				group.isEmpty() ? "Unknown group" : group,
				diff.toTotalMonths() + " months, " + diff.getDays() + " days");
	}

}
