package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;
import io.github.likcoras.asuka.handler.response.SilentSkyXMLResponse;
import io.github.likcoras.asuka.handler.response.SilentSkyXMLUpdateResponse;
import lombok.Value;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class SilentSkyXMLHandler extends TranslatingHandler {

	private static final String XML_LINK = "http://everath.net/sslist/csparse.xml";
	private static final String UPDATE_LINK = "http://everath.net/sslist/index.php";
	
	private Map<String, SilentXMLData> xmlData = new HashMap<>();
	private long updateTime = -1L;
	
	@Override
	public BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event) throws HandlerException {
		try {
			return onHandle(bot, event);
		} catch (IOException | XMLStreamException e) {
			throw new HandlerException(this, "Exception while fetching data", e);
		}
	}
	
	@Override
	public BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event) throws HandlerException {
		try {
			return onHandle(bot, event);
		} catch (IOException | XMLStreamException e) {
			throw new HandlerException(this, "Exception while fetching data", e);
		}
	}
	
	private BotResponse onHandle(AsukaBot bot, GenericMessageEvent<PircBotX> event) throws IOException, XMLStreamException, PermissionException {
		if (event.getTimestamp() - updateTime > 3600000L)
			update(event);
		String message = event.getMessage();
		if(BotUtil.isTrigger(message, "update"))
			if (bot.getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
				return update(event);
			else
				throw new PermissionException(this, event.getUser(), message);
		else if (message.length() > 1 && isTrigger(message))
			return getData(event, message.substring(1));
		return EmptyResponse.get();
	}
	
	private boolean isTrigger(String message) {
		synchronized (xmlData) {
			return xmlData.containsKey(message.substring(1));
		}
	}
	
	public BotResponse update(GenericMessageEvent<PircBotX> event) throws IOException, XMLStreamException {
		new URL(UPDATE_LINK).openConnection().connect();
		XMLStreamReader xmlReader = XMLInputFactory.newFactory().createXMLStreamReader(new URL(XML_LINK).openStream());
		synchronized (xmlData) {
			xmlData.clear();
			xmlData.putAll(parseData(xmlReader));
		}
		return new SilentSkyXMLUpdateResponse(event);
	}
	
	private Map<String, SilentXMLData> parseData(XMLStreamReader xml) throws XMLStreamException {
		Map<String, SilentXMLData> newData = new HashMap<>();
		String trigger = "";
		String project = "";
		int chapter = 0;
		String mediafireLink = "";
		String megaLink = "";
		String readerLink = "";
		while (xml.hasNext()) {
			xml.next();
			if (!xml.isStartElement())
				continue;
			String name = xml.getLocalName();
			switch (name) {
			case "trigger": {
				if (!trigger.isEmpty())
					newData.put(trigger, new SilentXMLData(project, chapter, mediafireLink, megaLink, readerLink));
				trigger = xml.getElementText().substring(1);
				break;
			}
			case "Project": project = xml.getElementText(); break;
			case "Chapter_no": chapter = Integer.parseInt(xml.getElementText()); break;
			case "Mediafire": mediafireLink = xml.getElementText(); break;
			case "Mega": megaLink = xml.getElementText(); break;
			case "Online": readerLink = xml.getElementText();
			}
		}
		return newData;
	}
	
	public BotResponse getData(GenericMessageEvent<PircBotX> event, String trigger) {
		synchronized (xmlData) {
			return new SilentSkyXMLResponse(event, xmlData.get(trigger));
		}
	}
	
	@Value
	public static class SilentXMLData {
		private String project;
		private int chapter;
		private String mediafireLink;
		private String megaLink;
		private String readerLink;
	}
	
}
