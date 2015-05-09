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
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SuperOpEvent;
import org.pircbotx.hooks.events.VoiceEvent;

public class AuthListener extends ListenerAdapter<PircBotX> {
	
	private final CustomUserPrefixHandler customPrefix;
	
	public AuthListener(final CustomUserPrefixHandler customPrefix) {
		
		this.customPrefix = customPrefix;
		
	}
	
	@Override
	public void onServerResponse(final ServerResponseEvent<PircBotX> eve) {
		
		if (eve.getCode() == 005)
			customPrefix.parsePrefix(eve.getParsedResponse());
		else if (eve.getCode() == 353)
			customPrefix.parseNames(eve.getParsedResponse());
		
	}
	
	@Override
	public void onNickChange(final NickChangeEvent<PircBotX> eve) {
		
		customPrefix.swapNick(eve.getOldNick(), eve.getNewNick());
		
	}
	
	@Override
	public void onPart(final PartEvent<PircBotX> eve) {
		
		customPrefix.delUser(eve.getUser(), eve.getChannel());
		
	}
	
	@Override
	public void onKick(final KickEvent<PircBotX> eve) {
		
		customPrefix.delUser(eve.getRecipient(), eve.getChannel());
		
	}
	
	@Override
	public void onOwner(final OwnerEvent<PircBotX> eve) {
		
		customPrefix.modUser(eve.getRecipient(), eve.getChannel(),
			UserLevel.OWNER, eve.isOwner());
		
	}
	
	@Override
	public void onSuperOp(final SuperOpEvent<PircBotX> eve) {
		
		customPrefix.modUser(eve.getRecipient(), eve.getChannel(),
			UserLevel.SUPEROP, eve.isSuperOp());
		
	}
	
	@Override
	public void onOp(final OpEvent<PircBotX> eve) {
		
		customPrefix.modUser(eve.getRecipient(), eve.getChannel(),
			UserLevel.OP, eve.isOp());
		
	}
	
	@Override
	public void onHalfOp(final HalfOpEvent<PircBotX> eve) {
		
		customPrefix.modUser(eve.getRecipient(), eve.getChannel(),
			UserLevel.HALFOP, eve.isHalfOp());
		
	}
	
	@Override
	public void onVoice(final VoiceEvent<PircBotX> eve) {
		
		customPrefix.modUser(eve.getRecipient(), eve.getChannel(),
			UserLevel.VOICE, eve.hasVoice());
		
	}
	
}
