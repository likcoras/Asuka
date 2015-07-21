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
	public void configure(BotConfig config) throws ConfigException {
	}

	@Override
	public BotResponse handle(AsukaBot bot, Event<PircBotX> event) throws PermissionException, HandlerException {
		if (event instanceof ActionEvent)
			return onAction(bot, (ActionEvent<PircBotX>) event);
		else if (event instanceof ChannelInfoEvent)
			return onChannelInfo(bot, (ChannelInfoEvent<PircBotX>) event);
		else if (event instanceof ConnectEvent)
			return onConnect(bot, (ConnectEvent<PircBotX>) event);
		else if (event instanceof DisconnectEvent)
			return onDisconnect(bot, (DisconnectEvent<PircBotX>) event);
		else if (event instanceof FingerEvent)
			return onFinger(bot, (FingerEvent<PircBotX>) event);
		else if (event instanceof HalfOpEvent)
			return onHalfOp(bot, (HalfOpEvent<PircBotX>) event);
		else if (event instanceof IncomingChatRequestEvent)
			return onIncomingChatRequest(bot, (IncomingChatRequestEvent<PircBotX>) event);
		else if (event instanceof IncomingFileTransferEvent)
			return onIncomingFileTransfer(bot, (IncomingFileTransferEvent<PircBotX>) event);
		else if (event instanceof InviteEvent)
			return onInvite(bot, (InviteEvent<PircBotX>) event);
		else if (event instanceof JoinEvent)
			return onJoin(bot, (JoinEvent<PircBotX>) event);
		else if (event instanceof KickEvent)
			return onKick(bot, (KickEvent<PircBotX>) event);
		else if (event instanceof MessageEvent)
			return onMessage(bot, (MessageEvent<PircBotX>) event);
		else if (event instanceof ModeEvent)
			return onMode(bot, (ModeEvent<PircBotX>) event);
		else if (event instanceof MotdEvent)
			return onMotd(bot, (MotdEvent<PircBotX>) event);
		else if (event instanceof NickAlreadyInUseEvent)
			return onNickAlreadyInUse(bot, (NickAlreadyInUseEvent<PircBotX>) event);
		else if (event instanceof NickChangeEvent)
			return onNickChange(bot, (NickChangeEvent<PircBotX>) event);
		else if (event instanceof NoticeEvent)
			return onNotice(bot, (NoticeEvent<PircBotX>) event);
		else if (event instanceof OpEvent)
			return onOp(bot, (OpEvent<PircBotX>) event);
		else if (event instanceof OwnerEvent)
			return onOwner(bot, (OwnerEvent<PircBotX>) event);
		else if (event instanceof PartEvent)
			return onPart(bot, (PartEvent<PircBotX>) event);
		else if (event instanceof PingEvent)
			return onPing(bot, (PingEvent<PircBotX>) event);
		else if (event instanceof PrivateMessageEvent)
			return onPrivateMessage(bot, (PrivateMessageEvent<PircBotX>) event);
		else if (event instanceof QuitEvent)
			return onQuit(bot, (QuitEvent<PircBotX>) event);
		else if (event instanceof RemoveChannelBanEvent)
			return onRemoveChannelBan(bot, (RemoveChannelBanEvent<PircBotX>) event);
		else if (event instanceof RemoveChannelKeyEvent)
			return onRemoveChannelKey(bot, (RemoveChannelKeyEvent<PircBotX>) event);
		else if (event instanceof RemoveChannelLimitEvent)
			return onRemoveChannelLimit(bot, (RemoveChannelLimitEvent<PircBotX>) event);
		else if (event instanceof RemoveInviteOnlyEvent)
			return onRemoveInviteOnly(bot, (RemoveInviteOnlyEvent<PircBotX>) event);
		else if (event instanceof RemoveModeratedEvent)
			return onRemoveModerated(bot, (RemoveModeratedEvent<PircBotX>) event);
		else if (event instanceof RemoveNoExternalMessagesEvent)
			return onRemoveNoExternalMessages(bot, (RemoveNoExternalMessagesEvent<PircBotX>) event);
		else if (event instanceof RemovePrivateEvent)
			return onRemovePrivate(bot, (RemovePrivateEvent<PircBotX>) event);
		else if (event instanceof RemoveSecretEvent)
			return onRemoveSecret(bot, (RemoveSecretEvent<PircBotX>) event);
		else if (event instanceof RemoveTopicProtectionEvent)
			return onRemoveTopicProtection(bot, (RemoveTopicProtectionEvent<PircBotX>) event);
		else if (event instanceof ServerPingEvent)
			return onServerPing(bot, (ServerPingEvent<PircBotX>) event);
		else if (event instanceof ServerResponseEvent)
			return onServerResponse(bot, (ServerResponseEvent<PircBotX>) event);
		else if (event instanceof SetChannelBanEvent)
			return onSetChannelBan(bot, (SetChannelBanEvent<PircBotX>) event);
		else if (event instanceof SetChannelKeyEvent)
			return onSetChannelKey(bot, (SetChannelKeyEvent<PircBotX>) event);
		else if (event instanceof SetChannelLimitEvent)
			return onSetChannelLimit(bot, (SetChannelLimitEvent<PircBotX>) event);
		else if (event instanceof SetInviteOnlyEvent)
			return onSetInviteOnly(bot, (SetInviteOnlyEvent<PircBotX>) event);
		else if (event instanceof SetModeratedEvent)
			return onSetModerated(bot, (SetModeratedEvent<PircBotX>) event);
		else if (event instanceof SetNoExternalMessagesEvent)
			return onSetNoExternalMessages(bot, (SetNoExternalMessagesEvent<PircBotX>) event);
		else if (event instanceof SetPrivateEvent)
			return onSetPrivate(bot, (SetPrivateEvent<PircBotX>) event);
		else if (event instanceof SetSecretEvent)
			return onSetSecret(bot, (SetSecretEvent<PircBotX>) event);
		else if (event instanceof SetTopicProtectionEvent)
			return onSetTopicProtection(bot, (SetTopicProtectionEvent<PircBotX>) event);
		else if (event instanceof SocketConnectEvent)
			return onSocketConnect(bot, (SocketConnectEvent<PircBotX>) event);
		else if (event instanceof SuperOpEvent)
			return onSuperOp(bot, (SuperOpEvent<PircBotX>) event);
		else if (event instanceof TimeEvent)
			return onTime(bot, (TimeEvent<PircBotX>) event);
		else if (event instanceof TopicEvent)
			return onTopic(bot, (TopicEvent<PircBotX>) event);
		else if (event instanceof UnknownEvent)
			return onUnknown(bot, (UnknownEvent<PircBotX>) event);
		else if (event instanceof UserListEvent)
			return onUserList(bot, (UserListEvent<PircBotX>) event);
		else if (event instanceof UserModeEvent)
			return onUserMode(bot, (UserModeEvent<PircBotX>) event);
		else if (event instanceof VersionEvent)
			return onVersion(bot, (VersionEvent<PircBotX>) event);
		else if (event instanceof VoiceEvent)
			return onVoice(bot, (VoiceEvent<PircBotX>) event);
		else if (event instanceof WhoisEvent)
			return onWhois(bot, (WhoisEvent<PircBotX>) event);
		return EmptyResponse.get();
	}

	protected BotResponse onAction(AsukaBot bot, ActionEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onChannelInfo(AsukaBot bot, ChannelInfoEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onConnect(AsukaBot bot, ConnectEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onDisconnect(AsukaBot bot, DisconnectEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onFinger(AsukaBot bot, FingerEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onHalfOp(AsukaBot bot, HalfOpEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onIncomingChatRequest(AsukaBot bot, IncomingChatRequestEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onIncomingFileTransfer(AsukaBot bot, IncomingFileTransferEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onInvite(AsukaBot bot, InviteEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onJoin(AsukaBot bot, JoinEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onKick(AsukaBot bot, KickEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onMessage(AsukaBot bot, MessageEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onMode(AsukaBot bot, ModeEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onMotd(AsukaBot bot, MotdEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onNickAlreadyInUse(AsukaBot bot, NickAlreadyInUseEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onNickChange(AsukaBot bot, NickChangeEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onNotice(AsukaBot bot, NoticeEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onOp(AsukaBot bot, OpEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onOwner(AsukaBot bot, OwnerEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onPart(AsukaBot bot, PartEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onPing(AsukaBot bot, PingEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onPrivateMessage(AsukaBot bot, PrivateMessageEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onQuit(AsukaBot bot, QuitEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveChannelBan(AsukaBot bot, RemoveChannelBanEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveChannelKey(AsukaBot bot, RemoveChannelKeyEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveChannelLimit(AsukaBot bot, RemoveChannelLimitEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveInviteOnly(AsukaBot bot, RemoveInviteOnlyEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveModerated(AsukaBot bot, RemoveModeratedEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveNoExternalMessages(AsukaBot bot, RemoveNoExternalMessagesEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemovePrivate(AsukaBot bot, RemovePrivateEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveSecret(AsukaBot bot, RemoveSecretEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onRemoveTopicProtection(AsukaBot bot, RemoveTopicProtectionEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onServerPing(AsukaBot bot, ServerPingEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onServerResponse(AsukaBot bot, ServerResponseEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetChannelBan(AsukaBot bot, SetChannelBanEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetChannelKey(AsukaBot bot, SetChannelKeyEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetChannelLimit(AsukaBot bot, SetChannelLimitEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetInviteOnly(AsukaBot bot, SetInviteOnlyEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetModerated(AsukaBot bot, SetModeratedEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetNoExternalMessages(AsukaBot bot, SetNoExternalMessagesEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetPrivate(AsukaBot bot, SetPrivateEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetSecret(AsukaBot bot, SetSecretEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSetTopicProtection(AsukaBot bot, SetTopicProtectionEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSocketConnect(AsukaBot bot, SocketConnectEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onSuperOp(AsukaBot bot, SuperOpEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onTime(AsukaBot bot, TimeEvent<PircBotX> event) throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onTopic(AsukaBot bot, TopicEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onUnknown(AsukaBot bot, UnknownEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onUserList(AsukaBot bot, UserListEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onUserMode(AsukaBot bot, UserModeEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onVersion(AsukaBot bot, VersionEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onVoice(AsukaBot bot, VoiceEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

	protected BotResponse onWhois(AsukaBot bot, WhoisEvent<PircBotX> event)
			throws PermissionException, HandlerException {
		return EmptyResponse.get();
	}

}
