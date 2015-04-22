package io.github.likcoras.ssbot;

import io.github.likcoras.ssbot.backends.BttHandler;
import io.github.likcoras.ssbot.backends.DbHandler;
import io.github.likcoras.ssbot.backends.MuHandler;
import io.github.likcoras.ssbot.backends.XmlHandler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.SocketConnectEvent;

public class BotManager extends ListenerAdapter<PircBotX> {
	
	private final DbHandler db;
	private final MuHandler mu;
	private final XmlHandler xml;
	private final BttHandler bt;
	
	private final PircBotX bot;
	
	private final Pattern mup;
	
	private final Pattern btp;
	
	public BotManager(final ConfigParser cfg, final DbHandler db,
			final MuHandler mu, final XmlHandler xml, final BttHandler bt) {
		
		this.db = db;
		this.mu = mu;
		this.xml = xml;
		this.bt = bt;
		
		mup =
				Pattern.compile("(http(s)?://)?(www\\.)?((rlstrackr.com/series/info/)|(mangaupdates.com/series.html\\?id=))(\\d+)(/)?");
		btp =
				Pattern.compile("((http(s)?://)?bato.to/comic/_/(comics/)?\\S+(/)?)");
		
		final Builder<PircBotX> build = new Configuration.Builder<PircBotX>();
		
		build.addListener(this)
				.setAutoReconnect(true)
				.setName(cfg.getString("ircnick"))
				.setLogin(cfg.getString("irclogin"))
				.setRealName(cfg.getString("ircrealname"))
				.setServer(cfg.getString("irchost"),
						Integer.parseInt(cfg.getString("ircport")));
		
		if (Boolean.parseBoolean(cfg.getString("ircssl"))) {
			
			build.setSocketFactory(new UtilSSLSocketFactory()
					.trustAllCertificates());
			
		}
		
		if (!cfg.getString("ircnickserv").isEmpty()) {
			
			build.setNickservPassword(cfg.getString("ircnickserv"));
			
		}
		
		if (!cfg.getString("ircpass").isEmpty()) {
			
			build.setNickservPassword(cfg.getString("ircpass"));
			
		}
		
		final String[] rch = cfg.getString("ircchannels").split(",");
		for (final String s : rch) {
			
			final String[] ss = s.split(":");
			
			if (ss.length > 1) {
				
				build.addAutoJoinChannel(ss[0], ss[1]);
				
			} else {
				
				build.addAutoJoinChannel(s);
				
			}
			
		}
		
		bot = new PircBotX(build.buildConfiguration());
		
		try {
			
			bot.startBot();
			
		} catch (IrcException | IOException e) {
			
			System.out.println("Error: Exception while starting bot!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public void onConnect(final ConnectEvent<PircBotX> eve) {
		
		System.out.println("Connected!");
		
	}
	
	@Override
	public void onDisconnect(final DisconnectEvent<PircBotX> eve) {
		
		System.out.println("Disconnected!");
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> eve) {
		
		final String msg = eve.getMessage();
		
		final Matcher mum = mup.matcher(msg);
		final Matcher btm = btp.matcher(msg);
		
		if (mum.find()) {
			
			sendMu(eve.getChannel(), mu.getData(Integer.parseInt(mum.group(7))));
			
		} else if (btm.find()) {
			
			sendBt(eve.getChannel(), bt.getData(btm.group(1)));
			
		} else if (msg.startsWith("!batoto ") || msg.startsWith(".batoto ")) {
			
			final String[] data = bt.search(msg.substring(8));
			
			if (data != null) {
				
				sendBt(eve.getChannel(), data);
				
			} else {
				
				eve.getChannel()
						.send()
						.message("Nothing found for '" + msg.substring(8) + "'");
				
			}
			
		} else if (msg.startsWith("!baka ") || msg.startsWith(".baka ")) {
			
			final int i = db.getId(msg.substring(6).split(" "));
			
			if (i > -1) {
				
				sendMu(eve.getChannel(), mu.getData(i));
				
			} else {
				
				eve.getChannel()
						.send()
						.message("Nothing found for '" + msg.substring(6) + "'");
				
			}
			
		} else if (msg.matches("(\\.|!)quit")) {
			
			if (eve.getChannel().getOps().contains(eve.getUser())
					|| eve.getChannel().getOwners().contains(eve.getUser())
					|| eve.getChannel().getSuperOps().contains(eve.getUser())) {
				
				eve.getChannel().send().message("Understood, quitting.");
				bot.stopBotReconnect();
				bot.sendIRC().quitServer("Requested");
				
			}
			
		} else if (msg.matches("(\\.|!)update")) {
			
			if (eve.getChannel().getOps().contains(eve.getUser())
					|| eve.getChannel().getOwners().contains(eve.getUser())
					|| eve.getChannel().getSuperOps().contains(eve.getUser())) {
				
				eve.getChannel().send().message("Understood, updating.");
				xml.update();
				
			}
			
		} else if (msg.startsWith("!")) {
			
			final String[] data = xml.getData(msg.substring(1));
			
			if (data != null) {
				
				sendXml(eve.getChannel(), data);
				
			}
			
		}
		
	}
	
	@Override
	public void onSocketConnect(final SocketConnectEvent<PircBotX> eve) {
		
		System.out.println("Connecting...");
		
	}
	
	private void sendBt(final Channel ch, final String[] data) {
		
		ch.send()
				.message(
						String.format(
								"%sName: %s%s | "
										+ "%sAuthor: %s%s | %sTags: %s%s | %sStatus: %s%s | %sLink: %s%s",
								Colors.BOLD, Colors.BOLD, data[0], Colors.BOLD,
								Colors.BOLD, data[1], Colors.BOLD, Colors.BOLD,
								data[2], Colors.BOLD, Colors.BOLD, data[3],
								Colors.BOLD, Colors.BOLD, data[4], Colors.BOLD,
								Colors.BOLD, data[4]));
		
	}
	
	private void sendMu(final Channel ch, final String[] data) {
		
		ch.send()
				.message(
						String.format(
								"%sName: %s%s | "
										+ "%sAuthor: %s%s | %sTags: %s%s | %sLatest Release: %s%s "
										+ "by %s (%s days ago) | %sLink: %s%s",
								Colors.BOLD, Colors.BOLD, data[0], Colors.BOLD,
								Colors.BOLD, data[1], Colors.BOLD, Colors.BOLD,
								data[2], Colors.BOLD, Colors.BOLD, data[3],
								data[5], data[4], Colors.BOLD, Colors.BOLD,
								data[6]));
		
	}
	
	private void sendXml(final Channel ch, final String[] data) {
		
		ch.send()
				.message(
						String.format(
								"%s%s%s chapter %s%s%s | %sMega: %s%s | %sMediafire: %s%s | %sReader: %s%s",
								Colors.BOLD, data[0], Colors.BOLD, Colors.BOLD,
								data[1], Colors.BOLD, Colors.BOLD, Colors.BOLD,
								data[2], Colors.BOLD, Colors.BOLD, data[3],
								Colors.BOLD, Colors.BOLD, data[4]));
		
	}
	
}
