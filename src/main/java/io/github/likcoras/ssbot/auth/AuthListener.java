package io.github.likcoras.ssbot.auth;

import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.HalfOpEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.OwnerEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SuperOpEvent;
import org.pircbotx.hooks.events.VoiceEvent;

public class AuthListener extends ListenerAdapter<PircBotX> {
	
	private final CustomUserPrefixHandler customPrefix;
	
	public AuthListener(final AuthHandler auth) {
		
		customPrefix = auth.getCustomPrefix();
		
	}
	
	@Override
	public void onServerResponse(final ServerResponseEvent<PircBotX> eve) {
		
		if (eve.getCode() == 005)
			customPrefix.loadPrefix(eve.getParsedResponse());
		else if (eve.getCode() == 353)
			customPrefix.loadNames(eve.getParsedResponse());
		
	}
	
	@Override
	public void onNickChange(final NickChangeEvent<PircBotX> eve) {
		
		customPrefix.swapNick(eve.getOldNick(), eve.getNewNick());
		
	}
	
	@Override
	public void onPart(final PartEvent<PircBotX> eve) {
		
		customPrefix.delUser(eve.getChannel(), eve.getUser());
		
	}
	
	@Override
	public void onKick(final KickEvent<PircBotX> eve) {
		
		customPrefix.delUser(eve.getChannel(), eve.getRecipient());
		
	}
	
	@Override
	public void onQuit(final QuitEvent<PircBotX> eve) {
		
		customPrefix.delUserAll(eve.getUser());
		
	}
	
	@Override
	public void onOwner(final OwnerEvent<PircBotX> eve) {
		
		customPrefix.toggleLevel(eve.getChannel(), eve.getRecipient(),
			UserLevel.OWNER, eve.isOwner());
		
	}
	
	@Override
	public void onSuperOp(final SuperOpEvent<PircBotX> eve) {
		
		customPrefix.toggleLevel(eve.getChannel(), eve.getRecipient(),
			UserLevel.SUPEROP, eve.isSuperOp());
		
	}
	
	@Override
	public void onOp(final OpEvent<PircBotX> eve) {
		
		customPrefix.toggleLevel(eve.getChannel(), eve.getRecipient(),
			UserLevel.OP, eve.isOp());
		
	}
	
	@Override
	public void onHalfOp(final HalfOpEvent<PircBotX> eve) {
		
		customPrefix.toggleLevel(eve.getChannel(), eve.getRecipient(),
			UserLevel.HALFOP, eve.isHalfOp());
		
	}
	
	@Override
	public void onVoice(final VoiceEvent<PircBotX> eve) {
		
		customPrefix.toggleLevel(eve.getChannel(), eve.getRecipient(),
			UserLevel.VOICE, eve.hasVoice());
		
	}
	
}
