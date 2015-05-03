package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.backends.exceptions.InvalidHandlerException;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;
import io.github.likcoras.ssbot.data.XmlData;
import io.github.likcoras.ssbot.data.XmlUpdateData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlHandler implements DataHandler {
	
	private static final Logger HANDLE = Logger.getLogger("Handler");
	
	private static final Pattern HANDLE_PATTERN = Pattern
		.compile("^![a-z]+\\d+$");
	
	private final String link;
	private final Map<String, XmlData> data;
	
	public XmlHandler(final ConfigParser cfg) {
		
		link = cfg.getProperty("xmlfile");
		data = new HashMap<String, XmlData>();
		
	}
	
	@Override
	public boolean isHandlerOf(final String query) {
		
		return query.equals(".update")
			|| HandlerUtils.checkHandler(query, HANDLE_PATTERN)
			&& data.containsKey(query.substring(1));
		
	}
	
	@Override
	public XmlData getData(final String query) throws NoResultsException {
		
		if (!isHandlerOf(query))
			throw new InvalidHandlerException(query);
		else if (query.equals(".update"))
			try {
				
				update();
				return new XmlUpdateData();
				
			} catch (IOException | SAXException | ParserConfigurationException e) {
				
				throw new NoResultsException(query, e);
				
			}
		
		XmlData result;
		
		synchronized (data) {
			result = data.get(query.substring(1));
		}
		
		if (result == null)
			throw new NoResultsException(query);
		
		HANDLE
			.info("Query '" + query + "' returned data: " + result.toString());
		return result;
		
	}
	
	public void update() throws IOException, SAXException,
		ParserConfigurationException {
		
		HANDLE.info("Updating xml...");
		
		final Map<String, XmlData> tmpData = new HashMap<String, XmlData>();
		addData(tmpData);
		
		synchronized (data) {
			
			data.clear();
			data.putAll(tmpData);
			
		}
		
	}
	
	private void addData(final Map<String, XmlData> map) throws IOException,
		SAXException, ParserConfigurationException {
		
		final NodeList chapters = getDocument().getFirstChild().getChildNodes();
		for (int i = 0; i < chapters.getLength(); i++) {
			
			final XmlData xml = new XmlData();
			final Node chapter = chapters.item(i);
			addChapter(xml, chapter);
			
			map.put(chapter.getNodeName(), xml);
			
		}
		
	}
	
	private void addChapter(final XmlData xml, final Node chapter) {
		
		final NodeList chapterData = chapter.getChildNodes();
		
		for (int i = 0; i < chapterData.getLength(); i++) {
			
			final Node dataValue = chapterData.item(i);
			final String name = dataValue.getNodeName();
			
			if (name.equals("Project"))
				xml.setTitle(dataValue.getTextContent());
			else if (name.equals("Chapter_no"))
				xml.setChapter(dataValue.getTextContent());
			else if (name.equals("Mediafire"))
				xml.setLinkMediafire(dataValue.getTextContent());
			else if (name.equals("Mega"))
				xml.setLinkMega(dataValue.getTextContent());
			else if (name.equals("Online"))
				xml.setLinkReader(dataValue.getTextContent());
			
		}
		
	}
	
	private Document getDocument() throws IOException, SAXException,
		ParserConfigurationException {
		
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
			.parse(link);
		
	}
	
}
