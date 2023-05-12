package com.zeptolab.zeptolabchatservice.handler;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.zeptolab.zeptolabchatservice.data.ChatEvent;
import com.zeptolab.zeptolabchatservice.data.EmptyEvent;
import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.data.LoginEvent;
import com.zeptolab.zeptolabchatservice.data.UserChannelEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.service.ChannelService;
import com.zeptolab.zeptolabchatservice.service.ChatService;
import com.zeptolab.zeptolabchatservice.service.MessageService;
import com.zeptolab.zeptolabchatservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ChatHandler implements EventReceived {


    private final ChatService chatService;

    private final UserService userService;

    private final MessageService messageService;

    private final ChannelService channelService;

    private static final String READ_MESSAGE = "read_message";


    public ChatHandler(final SocketIOServer server,
                       final ChatService chatService,
                       final UserService userService,
                       final MessageService messageService,
                       final ChannelService channelService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
        this.channelService = channelService;
        addListeners(server);
    }

    private void addListeners(final SocketIOServer server) {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(NameSpace.LOGIN.getStringValue(), LoginEvent.class, onLoginEvent());
        server.addEventListener(NameSpace.JOIN.getStringValue(), JoinEvent.class, onChannelJoinEvent());
        server.addEventListener(NameSpace.LEAVE.getStringValue(), EmptyEvent.class, onChannelLeaveEvent());
        server.addEventListener(NameSpace.DISCONNECT.getStringValue(), EmptyEvent.class, onDisconnectEvent());
        server.addEventListener(NameSpace.LIST.getStringValue(), EmptyEvent.class, onGetChannelsListEvent());
        server.addEventListener(NameSpace.USER.getStringValue(), UserChannelEvent.class, onGetUserListEvent());
        server.addEventListener(NameSpace.CHAT.getStringValue(), ChatEvent.class, onChatReceived());
    }


    @Override
    public DataListener<LoginEvent> onLoginEvent() {
        return (client, data, ackSender) -> {
            final Device device = new Device(client.getRemoteAddress().toString());
            final Optional<User> hasAccount = userService.insertOrUpdate(data, device, client.getSessionId().toString());
            if (hasAccount.isEmpty()) {
                return;
            }
            if (hasAccount.get().getChannel() != null) {
                final Optional<Channel> channel = channelService.getChannelById(hasAccount.get().getChannel().getId());
                if (channel.isPresent()) {
                    log.info("user joined to his latest channel");
                    client.joinRoom(channel.get().getName());
                }
            }

            log.info("new user has been connect");
            client.sendEvent(READ_MESSAGE, "Welcome " + data.name());
        };
    }

    @Override
    public DataListener<JoinEvent> onChannelJoinEvent() {
        return (client, data, ackSender) -> {
            final Optional<User> user = userService.getUserBySessionId(client.getSessionId().toString());
            if (user.isPresent()) {
                channelService.validateUserChannel(user.get(), data, client::leaveRoom);
                try {
                    final Channel channel = channelService.joinOrCreate(user.get(), data);
                    final List<ChatEvent> list = messageService.getMessagesByChannelId(channel.getId());
                    client.joinRoom(channel.getName());
                    client.sendEvent(READ_MESSAGE, list.toString());
                    log.info("user join to new channel");
                } catch (IllegalAccessException e){
                    log.warn("user is already in this channel");
                    client.sendEvent(READ_MESSAGE,"User is already in the channel");
                }
            }
        };
    }

    @Override
    public DataListener<EmptyEvent> onChannelLeaveEvent() {
        return (client, data, ackSender) -> {
            final Optional<User> user = userService.getUserBySessionId(client.getSessionId().toString());
            if (user.isPresent()) {
                userService.validateChannelAccess(user.get());
                final Optional<Channel> channel = channelService.getChannelById(user.get().getChannel().getId());
                if (channel.isPresent()) {
                    final String channelName = channel.get().getName();
                    final Optional<User> updatedUser = this.userService.terminateUserAccessToChannel(user.get());
                    if (updatedUser.isPresent()) {
                        client.leaveRoom(channelName);
                        client.sendEvent(READ_MESSAGE, "leaved from " + channelName);
                    }
                }
            }
        };
    }

    @Override
    public DataListener<EmptyEvent> onDisconnectEvent() {
        return (client, data, ackSender) -> {
            final Optional<User> user = userService.getUserBySessionId(client.getSessionId().toString());
            if (user.isPresent()) {
                client.disconnect();
            }
        };
    }

    @Override
    public DataListener<EmptyEvent> onGetChannelsListEvent() {
        return (client, data, ackSender) -> {
            final List<String> channels = channelService.getAllChannel();
            client.sendEvent(READ_MESSAGE, channels);
        };
    }

    @Override
    public DataListener<UserChannelEvent> onGetUserListEvent() {
        return (client, data, ackSender) -> {
            final List<String> list = channelService.getChannelUsers(data.channel());
            client.sendEvent(READ_MESSAGE, list);
        };
    }

    @Override
    public DataListener<ChatEvent> onChatReceived() {
        return (client, data, ackSender) -> {
            log.info("new chat event received");
            chatService.saveMessage(client, data);
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            final Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            log.info("onConnected {} ", params.toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            log.info("onDisconnected {} ", params.toString());
        };
    }


}
