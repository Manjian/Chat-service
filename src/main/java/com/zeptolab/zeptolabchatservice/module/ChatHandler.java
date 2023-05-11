package com.zeptolab.zeptolabchatservice.module;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeptolab.zeptolabchatservice.constants.Constants;
import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.data.LoginData;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.service.ChannelService;
import com.zeptolab.zeptolabchatservice.service.UserService;
import com.zeptolab.zeptolabchatservice.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ChatHandler {


    private final SocketIOServer server;
    private final SocketService socketService;

    private final UserService userService;

    private final ChannelService channelService;

    private final ObjectMapper objectMapper;


    public ChatHandler(final SocketIOServer server,
                       final SocketService socketService,
                       final UserService userService,
                       final ChannelService channelService,
                       final ObjectMapper objectMapper) {
        this.server = server;
        this.socketService = socketService;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.channelService = channelService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(Route.JOIN.getStringValue(), JoinEvent.class, onChatReceived());

    }

    private DataListener<JoinEvent> onChatReceived() {
        return (client, data, ackSender) -> {
            final User user = userService.getUserBySessionId(client.getSessionId().toString());
            final Channel channel = channelService.joinOrCreate(user, data);
            client.joinRoom(channel.getName());
            client.sendEvent("History",channel.getMessages());
        };
    }


    private ConnectListener onConnected() {
        return client -> {

            final Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            final Optional<String> described = String.join("", params.get(Route.LOGIN.getStringValue())).describeConstable();
            final boolean hasLogin = described.isPresent();

            if (!hasLogin || described.get().equals("undefined")) {
                client.disconnect();
                return;
            }

            final LoginData loginData;

            try {
                loginData = objectMapper.readValue(described.get(), LoginData.class);
                final Device device = new Device(client.getRemoteAddress().toString());

                final Optional<User> hasAccount = userService.insertOrUpdate(
                        loginData,
                        device,
                        client.getSessionId().toString());

                if (hasAccount.isEmpty()) {
                    client.disconnect();
                    return;
                }

                final Channel channel = hasAccount.get().getChannel();
                if (channel != null) {
                    client.joinRoom(channel.getName());
//                    socketService.saveInfoMessage(client,
//                            String.format(Constants.WELCOME_MESSAGE, hasAccount.get().getName()), channel.getName());
                }
                log.info("new user has been connect");

            } catch (JsonProcessingException e) {
                client.disconnect();
                throw new RuntimeException(e);
            }
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
//            String room = String.join("", params.get("room"));
//            String username = String.join("", params.get("username"));
//            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), room);
//            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }


}
