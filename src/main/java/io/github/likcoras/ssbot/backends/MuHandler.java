package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.data.MuData;
import io.github.likcoras.ssbot.data.Release;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MuHandler implements DataHandler {
	
	private static final Pattern SEARCH_PATTERN = Pattern
		.compile("^\\.baka\\s+(\\S+.*)");
	private static final Pattern LINK_PATTERN =
		Pattern
			.compile("((http(s)?://)?(www\\.)?((rlstrackr.com/series/info/)|(mangaupdates.com/series.html\\?id=))(\\d+)(/)?)");
	
	private final String base;
	
	private final DbHandler database;
	
	public MuHandler(final ConfigParser cfg) throws ClassNotFoundException {
		
		base = cfg.getProperty("muurl");
		database = new DbHandler(cfg);
		
	}
	
	@Override
	public boolean isHandlerOf(final String query) {
		
		return HandlerUtils.checkHandler(query, SEARCH_PATTERN, LINK_PATTERN);
		
	}
	
	@Override
	public MuData getData(final String query) throws NoResultsException {
		
		if (!isHandlerOf(query))
			throw new InvalidHandlerException(query);
		
		try {
			
			final Matcher searchMatch = SEARCH_PATTERN.matcher(query);
			if (searchMatch.find())
				return search(searchMatch.group(1));
			
			final Matcher linkMatch = LINK_PATTERN.matcher(query);
			if (linkMatch.find())
				return fromLink(linkMatch.group(1));
			
		} catch (final IOException e) {
			
			throw new NoResultsException(query, e);
			
		} catch (final SQLException e) {
			
			throw new NoResultsException(query, e);
			
		} catch (final ParseException e) {
			
			throw new NoResultsException(query, e);
			
		}
		
		throw new NoResultsException(query);
		
	}
	
	private MuData search(final String query) throws SQLException,
		NoResultsException, IOException, ParseException {
		
		final int id = database.getId(query.split("\\s"));
		return fromLink(base + id);
		
	}
	
	private MuData fromLink(final String link) throws IOException,
		ParseException {
		
		final Document doc = Jsoup.connect(link).get();
		final MuData mu = new MuData();
		
		mu.setLink(link);
		addTitle(mu, doc);
		parseSidebar(mu, doc);
		addLatest(mu, doc);
		
		return mu;
		
	}
	
	private void addTitle(final MuData mu, final Document doc) {
		
		mu.setTitle(doc.select("div.content > header > h2").text());
		
	}
	
	private void parseSidebar(final MuData mu, final Document doc) {
		
		final Elements sidebar =
			doc.getElementsByTag("dl").get(0).getElementsByTag("dd");
		
		for (Element header : sidebar) {
			
			final String text = header.text();
			
			if (text.equals("Author"))
				mu.setAuthor(header.nextElementSibling().text());
			else if (text.equals("Tags"))
				addTags(mu, header.nextElementSibling());
			
		}
		
	}
	
	private void addTags(final MuData mu, final Element element) {
		
		final Elements tags = element.getElementsByTag("a");
		for (final Element tag : tags)
			mu.addTag(tag.text());
		
	}
	
	private void addLatest(final MuData mu, final Document doc)
		throws ParseException {
		
		final Elements row =
			doc.select("table").get(0).getElementsByTag("tr").get(0)
				.getElementsByTag("td");
		
		final String group = row.get(2).text();
		final String chapter = row.get(0).text();
		final Date date = calcDays(row.get(1).text());
		
		mu.setLatest(new Release(group, chapter, date));
		
	}
	
	private Date calcDays(final String date) throws ParseException {
		
		return new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
		
	}
	
}
