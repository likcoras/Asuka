package io.github.likcoras.asuka;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericEvent;

import io.github.likcoras.asuka.handler.Handler;
import lombok.NonNull;
import lombok.SneakyThrows;

public final class BotUtil {

	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter
			.ofPattern("uuuu MMM dd hh:mm:ss a z", Locale.US).withZone(ZoneId.systemDefault());

	private BotUtil() {
	}

	public static String getId(@NonNull User user) {
		return user.getLogin() + "@" + user.getHostmask();
	}

	public static boolean isTrigger(@NonNull String message, @NonNull String trigger) {
		message = message.toLowerCase(Locale.ENGLISH);
		return message.startsWith("." + trigger) || message.startsWith("!" + trigger);
	}

	public static String addFormat(@NonNull String message) {
		return message.replaceAll("&b", "\u0002").replaceAll("&\u0002", "&b").replaceAll("&(\\d\\d)", "\u0003$1")
				.replaceAll("&\u0003", "&").replaceAll("&r", "\u000f").replaceAll("&\u000f", "&r")
				.replaceAll("&s", "\u0016").replaceAll("&\u0016", "&s").replaceAll("&i", "\u001d")
				.replaceAll("&\u001d", "&i").replaceAll("&u", "\u001f").replaceAll("&\u001f", "&u");
	}

	public static String formatTime(TemporalAccessor time) {
		return DEFAULT_FORMATTER.format(time);
	}

	@SneakyThrows(UnsupportedEncodingException.class)
	public static String urlEncode(String url) {
		return URLEncoder.encode(url, "UTF-8");
	}

	public static String getExceptionMessage(Handler handler, GenericEvent<PircBotX> event, Exception cause) {
		return "Error while handling " + event.getClass().getSimpleName() + " with "
				+ handler.getClass().getSimpleName() + ": " + cause.getMessage();
	}

}
