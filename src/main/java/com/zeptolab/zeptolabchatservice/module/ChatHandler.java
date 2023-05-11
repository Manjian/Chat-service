package com.zeptolab.zeptolabchatservice.module;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.data.EmptyEvent;
import com.zeptolab.zeptolabchatservice.data.LoginEvent;
import com.zeptolab.zeptolabchatservice.data.UserChannelEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.service.ChannelService;
import com.zeptolab.zeptolabchatservice.service.UserService;
import com.zeptolab.zeptolabchatservice.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ChatHandler implements EventReceived {


    private final SocketService socketService;

    private final UserService userService;

    private final ChannelService channelService;


    public ChatHandler(final SocketIOServer server,
                       final SocketService socketService,
                       final UserService userService,
                       final ChannelService channelService) {
        this.socketService = socketService;
        this.userService = userService;
        this.channelService = channelService;
        addListener(server);
    }

    private void addListener(final SocketIOServer server) {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(Route.LOGIN.getStringValue(), LoginEvent.class, onLoginEvent());
        server.addEventListener(Route.JOIN.getStringValue(), JoinEvent.class, onChannelJoinEven());
        server.addEventListener(Route.LEAVE.getStringValue(), EmptyEvent.class, onChannelLeaveEvent());
        server.addEventListener(Route.DISCONNECT.getStringValue(), EmptyEvent.class, onDisconnectEvent());
        server.addEventListener(Route.LIST.getStringValue(), EmptyEvent.class, onGetChannelsListEvent());
        server.addEventListener(Route.USER.getStringValue(), UserChannelEvent.class, OnGetUserListEvent());
    }


    @Override
    public DataListener<LoginEvent> onLoginEvent() {
        return (client, data, ackSender) -> {
            final Device device = new Device(client.getRemoteAddress().toString());
            final Optional<User> hasAccount = userService.insertOrUpdate(data, device, client.getSessionId().toString());
            if (hasAccount.isEmpty()) {
                return;
            }
            final Channel channel = hasAccount.get().getChannel();
            if (channel != null) {
                client.joinRoom(channel.getName());
            }
            log.info("new user has been connect");

            client.sendEvent("read_message", "Welcome " + data.name());
        };
    }

    @Override
    public DataListener<JoinEvent> onChannelJoinEven() {
        return (client, data, ackSender) -> {
            final Optional<User> user = userService.getUserBySessionId(client.getSessionId().toString());
            if (user.isPresent()) {
                final Channel channel = channelService.joinOrCreate(user.get(), data);
                final List<Message> list = channel.getMessages();
                client.joinRoom(channel.getName());
                client.sendEvent("read_message", list.toString());
            }

        };
    }

    @Override
    public DataListener<EmptyEvent> onChannelLeaveEvent() {
        return (client, data, ackSender) -> {
            final Optional<User> user = userService.getUserBySessionId(client.getSessionId().toString());
            if (user.isPresent()) {
                final Channel channel = user.get().getChannel();
                if (channel != null) {
                    final String channelName = channel.getName();
                    final Optional<User> updatedUser = this.userService.terminateUserAccessToChannel(user.get());
                    if (updatedUser.isPresent()) {
                        client.leaveRoom(channelName);
                        client.sendEvent("read_message", "leaved from " + channel.getName());
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
            client.sendEvent("read_message", channels);
        };
    }

    @Override
    public DataListener<UserChannelEvent> OnGetUserListEvent() {
        return (client, data, ackSender) -> {
            final List<String> list = userService.getUsersByChannel(data.channel());
            client.sendEvent("read_message", list);
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            final Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        };
    }


}
