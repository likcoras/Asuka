package io.github.likcoras.asuka.handler;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotUtil;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.handler.response.ExceptionResponse;
import io.github.likcoras.asuka.handler.response.PermissionResponse;
import io.github.likcoras.asuka.handler.response.SilentSkyXMLResponse;
import io.github.likcoras.asuka.handler.response.SilentSkyXMLUpdateResponse;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.types.GenericMessageEvent;

@Log4j2
public class SilentSkyXMLHandler extends Handler {

	private static final String XML_LINK = "http://everath.net/sslist/csparse.xml";
	private static final String UPDATE_LINK = "http://everath.net/sslist/index.php";

	private Map<String, SilentXMLData> xmlData = new HashMap<>();
	private volatile long updateTime = -1L;

	public SilentSkyXMLHandler(AsukaBot bot) throws ConfigException {
		super(bot);
	}

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) {
		try {
			onHandle(event);
		} catch (IOException | XMLStreamException e) {
			getBot().send(new ExceptionResponse(this, event, e));
			log.error(BotUtil.getExceptionMessage(this, event, e), e);
		}
	}

	private void onHandle(GenericMessageEvent<PircBotX> event) throws IOException, XMLStreamException {
		if (event.getTimestamp() - updateTime > 3600000L)
			update(event);
		String message = event.getMessage();
		if (BotUtil.isTrigger(message, "update"))
			if (getBot().getAuthManager().checkAccess(event.getUser(), UserLevel.OP))
				sendUpdate(event);
			else
				getBot().send(new PermissionResponse(event));
		else if (message.length() > 1 && isTrigger(message))
			getData(event, message.substring(1));
	}

	private boolean isTrigger(String message) {
		synchronized (xmlData) {
			return xmlData.containsKey(message.substring(1));
		}
	}

	private void sendUpdate(GenericMessageEvent<PircBotX> event) throws IOException, XMLStreamException {
		update(event);
		getBot().send(new SilentSkyXMLUpdateResponse(event));
	}

	private void update(GenericMessageEvent<PircBotX> event) throws IOException, XMLStreamException {
		new URL(UPDATE_LINK).openConnection().connect();
		XMLStreamReader xmlReader = XMLInputFactory.newFactory().createXMLStreamReader(new URL(XML_LINK).openStream());
		synchronized (xmlData) {
			xmlData.clear();
			xmlData.putAll(parseData(xmlReader));
		}
		updateTime = event.getTimestamp();
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
			case "trigger":
				if (!trigger.isEmpty())
					newData.put(trigger, new SilentXMLData(project, chapter, mediafireLink, megaLink, readerLink));
				trigger = xml.getElementText().substring(1);
				break;
			case "Project":
				project = xml.getElementText();
				break;
			case "Chapter_no":
				chapter = Integer.parseInt(xml.getElementText());
				break;
			case "Mediafire":
				mediafireLink = xml.getElementText();
				break;
			case "Mega":
				megaLink = xml.getElementText();
				break;
			case "Online":
				readerLink = xml.getElementText();
			}
		}
		return newData;
	}

	public void getData(GenericMessageEvent<PircBotX> event, String trigger) {
		synchronized (xmlData) {
			getBot().send(new SilentSkyXMLResponse(event, xmlData.get(trigger)));
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
