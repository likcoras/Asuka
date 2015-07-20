package io.github.likcoras.asuka.handler;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ChannelInfoEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.FingerEvent;
import org.pircbotx.hooks.events.HalfOpEvent;
import org.pircbotx.hooks.events.IncomingChatRequestEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.MotdEvent;
import org.pircbotx.hooks.events.NickAlreadyInUseEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.OwnerEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.RemoveChannelBanEvent;
import org.pircbotx.hooks.events.RemoveChannelKeyEvent;
import org.pircbotx.hooks.events.RemoveChannelLimitEvent;
import org.pircbotx.hooks.events.RemoveInviteOnlyEvent;
import org.pircbotx.hooks.events.RemoveModeratedEvent;
import org.pircbotx.hooks.events.RemoveNoExternalMessagesEvent;
import org.pircbotx.hooks.events.RemovePrivateEvent;
import org.pircbotx.hooks.events.RemoveSecretEvent;
import org.pircbotx.hooks.events.RemoveTopicProtectionEvent;
import org.pircbotx.hooks.events.ServerPingEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SetChannelBanEvent;
import org.pircbotx.hooks.events.SetChannelKeyEvent;
import org.pircbotx.hooks.events.SetChannelLimitEvent;
import org.pircbotx.hooks.events.SetInviteOnlyEvent;
import org.pircbotx.hooks.events.SetModeratedEvent;
import org.pircbotx.hooks.events.SetNoExternalMessagesEvent;
import org.pircbotx.hooks.events.SetPrivateEvent;
import org.pircbotx.hooks.events.SetSecretEvent;
import org.pircbotx.hooks.events.SetTopicProtectionEvent;
import org.pircbotx.hooks.events.SocketConnectEvent;
import org.pircbotx.hooks.events.SuperOpEvent;
import org.pircbotx.hooks.events.TimeEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.pircbotx.hooks.events.UnknownEvent;
import org.pircbotx.hooks.events.UserListEvent;
import org.pircbotx.hooks.events.UserModeEvent;
import org.pircbotx.hooks.events.VersionEvent;
import org.pircbotx.hooks.events.VoiceEvent;
import org.pircbotx.hooks.events.WhoisEvent;

import io.github.likcoras.asuka.AsukaBot;
import io.github.likcoras.asuka.BotConfig;
import io.github.likcoras.asuka.exception.ConfigException;
import io.github.likcoras.asuka.exception.HandlerException;
import io.github.likcoras.asuka.exception.PermissionException;
import io.github.likcoras.asuka.handler.response.BotResponse;
import io.github.likcoras.asuka.handler.response.EmptyResponse;

public abstract class TranslatingHandler implements Handler {

	@Override
	public abstract void configure(BotConfig config) throws ConfigException;

	@Override
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event)  throws PermissionException, HandlerException {
		if (event instanceof ActionEvent)
			return onAction((ActionEvent<PircBotX>) event);
		else if (event instanceof ChannelInfoEvent)
			return onChannelInfo((ChannelInfoEvent<PircBotX>) event);
		else if (event instanceof ConnectEvent)
			return onConnect((ConnectEvent<PircBotX>) event);
		else if (event instanceof DisconnectEvent)
			return onDisconnect((DisconnectEvent<PircBotX>) event);
		else if (event instanceof FingerEvent)
			return onFinger((FingerEvent<PircBotX>) event);
		else if (event instanceof HalfOpEvent)
			return onHalfOp((HalfOpEvent<PircBotX>) event);
		else if (event instanceof IncomingChatRequestEvent)
			return onIncomingChatRequest((IncomingChatRequestEvent<PircBotX>) event);
		else if (event instanceof IncomingFileTransferEvent)
			return onIncomingFileTransfer((IncomingFileTransferEvent<PircBotX>) event);
		else if (event instanceof InviteEvent)
			return onInvite((InviteEvent<PircBotX>) event);
		else if (event instanceof JoinEvent)
			return onJoin((JoinEvent<PircBotX>) event);
		else if (event instanceof KickEvent)
			return onKick((KickEvent<PircBotX>) event);
		else if (event instanceof MessageEvent)
			return onMessage((MessageEvent<PircBotX>) event);
		else if (event instanceof ModeEvent)
			return onMode((ModeEvent<PircBotX>) event);
		else if (event instanceof MotdEvent)
			return onMotd((MotdEvent<PircBotX>) event);
		else if (event instanceof NickAlreadyInUseEvent)
			return onNickAlreadyInUse((NickAlreadyInUseEvent<PircBotX>) event);
		else if (event instanceof NickChangeEvent)
			return onNickChange((NickChangeEvent<PircBotX>) event);
		else if (event instanceof NoticeEvent)
			return onNotice((NoticeEvent<PircBotX>) event);
		else if (event instanceof OpEvent)
			return onOp((OpEvent<PircBotX>) event);
		else if (event instanceof OwnerEvent)
			return onOwner((OwnerEvent<PircBotX>) event);
		else if (event instanceof PartEvent)
			return onPart((PartEvent<PircBotX>) event);
		else if (event instanceof PingEvent)
			return onPing((PingEvent<PircBotX>) event);
		else if (event instanceof PrivateMessageEvent)
			return onPrivateMessage((PrivateMessageEvent<PircBotX>) event);
		else if (event instanceof QuitEvent)
			return onQuit((QuitEvent<PircBotX>) event);
		else if (event instanceof RemoveChannelBanEvent)
			return onRemoveChannelBan((RemoveChannelBanEvent<PircBotX>) event);
		else if (event instanceof RemoveChannelKeyEvent)
			return onRemoveChannelKey((RemoveChannelKeyEvent<PircBotX>) event);
		else if (event instanceof RemoveChannelLimitEvent)
			return onRemoveChannelLimit((RemoveChannelLimitEvent<PircBotX>) event);
		else if (event instanceof RemoveInviteOnlyEvent)
			return onRemoveInviteOnly((RemoveInviteOnlyEvent<PircBotX>) event);
		else if (event instanceof RemoveModeratedEvent)
			return onRemoveModerated((RemoveModeratedEvent<PircBotX>) event);
		else if (event instanceof RemoveNoExternalMessagesEvent)
			return onRemoveNoExternalMessages((RemoveNoExternalMessagesEvent<PircBotX>) event);
		else if (event instanceof RemovePrivateEvent)
			return onRemovePrivate((RemovePrivateEvent<PircBotX>) event);
		else if (event instanceof RemoveSecretEvent)
			return onRemoveSecret((RemoveSecretEvent<PircBotX>) event);
		else if (event instanceof RemoveTopicProtectionEvent)
			return onRemoveTopicProtection((RemoveTopicProtectionEvent<PircBotX>) event);
		else if (event instanceof ServerPingEvent)
			return onServerPing((ServerPingEvent<PircBotX>) event);
		else if (event instanceof ServerResponseEvent)
			return onServerResponse((ServerResponseEvent<PircBotX>) event);
		else if (event instanceof SetChannelBanEvent)
			return onSetChannelBan((SetChannelBanEvent<PircBotX>) event);
		else if (event instanceof SetChannelKeyEvent)
			return onSetChannelKey((SetChannelKeyEvent<PircBotX>) event);
		else if (event instanceof SetChannelLimitEvent)
			return onSetChannelLimit((SetChannelLimitEvent<PircBotX>) event);
		else if (event instanceof SetInviteOnlyEvent)
			return onSetInviteOnly((SetInviteOnlyEvent<PircBotX>) event);
		else if (event instanceof SetModeratedEvent)
			return onSetModerated((SetModeratedEvent<PircBotX>) event);
		else if (event instanceof SetNoExternalMessagesEvent)
			return onSetNoExternalMessages((SetNoExternalMessagesEvent<PircBotX>) event);
		else if (event instanceof SetPrivateEvent)
			return onSetPrivate((SetPrivateEvent<PircBotX>) event);
		else if (event instanceof SetSecretEvent)
			return onSetSecret((SetSecretEvent<PircBotX>) event);
		else if (event instanceof SetTopicProtectionEvent)
			return onSetTopicProtection((SetTopicProtectionEvent<PircBotX>) event);
		else if (event instanceof SocketConnectEvent)
			return onSocketConnect((SocketConnectEvent<PircBotX>) event);
		else if (event instanceof SuperOpEvent)
			return onSuperOp((SuperOpEvent<PircBotX>) event);
		else if (event instanceof TimeEvent)
			return onTime((TimeEvent<PircBotX>) event);
		else if (event instanceof TopicEvent)
			return onTopic((TopicEvent<PircBotX>) event);
		else if (event instanceof UnknownEvent)
			return onUnknown((UnknownEvent<PircBotX>) event);
		else if (event instanceof UserListEvent)
			return onUserList((UserListEvent<PircBotX>) event);
		else if (event instanceof UserModeEvent)
			return onUserMode((UserModeEvent<PircBotX>) event);
		else if (event instanceof VersionEvent)
			return onVersion((VersionEvent<PircBotX>) event);
		else if (event instanceof VoiceEvent)
			return onVoice((VoiceEvent<PircBotX>) event);
		else if (event instanceof WhoisEvent)
			return onWhois((WhoisEvent<PircBotX>) event);
		return EmptyResponse.get();
	}

	protected BotResponse onAction(ActionEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onChannelInfo(ChannelInfoEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onConnect(ConnectEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onDisconnect(DisconnectEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onFinger(FingerEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onHalfOp(HalfOpEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onIncomingChatRequest(IncomingChatRequestEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onIncomingFileTransfer(IncomingFileTransferEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onInvite(InviteEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onJoin(JoinEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onKick(KickEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onMessage(MessageEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onMode(ModeEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onMotd(MotdEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onNickAlreadyInUse(NickAlreadyInUseEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onNickChange(NickChangeEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onNotice(NoticeEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onOp(OpEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onOwner(OwnerEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onPart(PartEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onPing(PingEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onPrivateMessage(PrivateMessageEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onQuit(QuitEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveChannelBan(RemoveChannelBanEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveChannelKey(RemoveChannelKeyEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveChannelLimit(RemoveChannelLimitEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveInviteOnly(RemoveInviteOnlyEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveModerated(RemoveModeratedEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveNoExternalMessages(RemoveNoExternalMessagesEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemovePrivate(RemovePrivateEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveSecret(RemoveSecretEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onRemoveTopicProtection(RemoveTopicProtectionEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onServerPing(ServerPingEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onServerResponse(ServerResponseEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetChannelBan(SetChannelBanEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetChannelKey(SetChannelKeyEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetChannelLimit(SetChannelLimitEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetInviteOnly(SetInviteOnlyEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetModerated(SetModeratedEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetNoExternalMessages(SetNoExternalMessagesEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetPrivate(SetPrivateEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetSecret(SetSecretEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSetTopicProtection(SetTopicProtectionEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSocketConnect(SocketConnectEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onSuperOp(SuperOpEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onTime(TimeEvent<PircBotX> event)  { return EmptyResponse.get(); }

	protected BotResponse onTopic(TopicEvent<PircBotX> event) { return EmptyResponse.get(); }

	protected BotResponse onUnknown(UnknownEvent<PircBotX> event) { return EmptyResponse.get(); }

	protected BotResponse onUserList(UserListEvent<PircBotX> event) { return EmptyResponse.get(); }

	protected BotResponse onUserMode(UserModeEvent<PircBotX> event) { return EmptyResponse.get(); }

	protected BotResponse onVersion(VersionEvent<PircBotX> event) { return EmptyResponse.get(); }

	protected BotResponse onVoice(VoiceEvent<PircBotX> event) { return EmptyResponse.get(); }

	protected BotResponse onWhois(WhoisEvent<PircBotX> event) { return EmptyResponse.get(); }

}
