package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.backends.exceptions.InvalidHandlerException;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;
import io.github.likcoras.ssbot.data.BttData;
import io.github.likcoras.ssbot.data.values.Status;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BttHandler implements DataHandler {
	
	private static final Logger HANDLE = Logger.getLogger("Handler");
	
	private static final Pattern SEARCH_PATTERN = Pattern
		.compile("^\\.batoto\\s+(\\S+.*)");
	private static final Pattern LINK_PATTERN = Pattern
		.compile("((http(s)?://)?bato.to/comic/_/(comics/)?\\S+(/)?)");
	
	private final String base;
	
	public BttHandler(final ConfigParser cfg) {
		
		base = cfg.getProperty("bturl");
		
	}
	
	@Override
	public boolean isHandlerOf(final String query) {
		
		return HandlerUtils.checkHandler(query, SEARCH_PATTERN, LINK_PATTERN);
		
	}
	
	@Override
	public BttData getData(final String query) throws NoResultsException {
		
		if (!isHandlerOf(query))
			throw new InvalidHandlerException(query);
		
		try {
			
			BttData btt = new BttData();
			
			final Matcher searchMatch = SEARCH_PATTERN.matcher(query);
			if (searchMatch.find())
				search(btt, searchMatch.group(1));
			
			final Matcher linkMatch = LINK_PATTERN.matcher(query);
			if (linkMatch.find())
				fromLink(btt, linkMatch.group(1));
			
			HANDLE.info("Query '" + query + "' returned data: "
				+ btt.toString());
			return btt;
			
		} catch (final IOException e) {
			
			throw new NoResultsException(query, e);
			
		}
		
	}
	
	private BttData search(BttData btt, final String query) throws IOException,
		NoResultsException {
		
		final Document doc = Jsoup.connect(base + query).get();
		final Elements elements = doc.select("strong > a[href]");
		
		if (elements.isEmpty())
			throw new NoResultsException(query);
		
		final Element link = elements.get(0);
		return fromLink(btt, link.attr("href"));
		
	}
	
	private BttData fromLink(BttData btt, final String link) throws IOException {
		
		final Document doc = connect(link);
		
		btt.setLink(link);
		addTitle(btt, doc);
		parseTable(btt, doc);
		
		return btt;
		
	}
	
	private Document connect(String link) throws IOException {
		
		if (!link.startsWith("http"))
			link = "http://" + link;
		
		return Jsoup.connect(link).get();
		
	}
	
	private void addTitle(final BttData btt, final Document doc) {
		
		btt.setTitle(doc.select("h1.ipsType_pagetitle").text());
		
	}
	
	private void parseTable(final BttData btt, final Document doc) {
		
		final Elements tableRows =
			doc.getElementsByTag("table").get(0)
				.getElementsByAttribute("style");
		
		for (final Element row : tableRows) {
			final String header = row.text();
			
			if (header.equals("Author:"))
				btt.setAuthor(row.nextElementSibling().text());
			else if (header.equals("Genres:"))
				addTags(btt, row.nextElementSibling());
			else if (header.equals("Status:"))
				addStatus(btt, row.nextElementSibling().text());
			
		}
		
	}
	
	private void addTags(final BttData btt, final Element element) {
		
		final Elements tags = element.getElementsByTag("a");
		for (final Element tag : tags)
			btt.addTag(tag.text());
		
	}
	
	private void addStatus(final BttData btt, final String status) {
		
		if (status.equals("Complete"))
			btt.setStatus(Status.COMPLETE);
		else
			btt.setStatus(Status.ONGOING);
		
	}
	
}
