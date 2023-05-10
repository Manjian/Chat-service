package com.zeptolab.zeptolabchatservice.module;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeptolab.zeptolabchatservice.constants.Constants;
import com.zeptolab.zeptolabchatservice.data.LoginData;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.service.LoginService;
import com.zeptolab.zeptolabchatservice.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SocketModule {


    private final SocketIOServer server;
    private final SocketService socketService;

    private final LoginService loginService;

    private final ObjectMapper objectMapper;

    private static final String CHANNEL = "channel";

    public SocketModule(final SocketIOServer server,
                        final SocketService socketService,
                        final LoginService loginService,
                        final ObjectMapper objectMapper) {
        this.server = server;
        this.socketService = socketService;
        this.objectMapper = objectMapper;
        this.loginService = loginService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(CHANNEL, Message.class, onChatReceived());

    }


    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
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

                final Optional<User> hasAccount = loginService.getByName(loginData.name());
                final User user;
                if (hasAccount.isPresent()) {
                    user = hasAccount.get();
                    if (Objects.equals(user.getPassword(), loginData.password())) {
                        user.addDevice(device);
                        loginService.save(user);
                        log.info(" new device has been inserted with {} user ", user.getName());
                    } else {
                        log.info("user login failed the pass not match");
                        client.disconnect();
                        return;
                    }
                } else {
                    user = new User(loginData.name(), loginData.password());
                    user.addDevice(device);
                    loginService.save(user);
                    log.info(" new user has been inserted with {} user ", user.getName());
                }




                String channel = String.join("", params.get(CHANNEL));



                client.joinRoom(channel);


//                socketService.saveInfoMessage(client, String.format(Constants.WELCOME_MESSAGE, username), room);
//                log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
            } catch (JsonProcessingException e) {
                client.disconnect();
                throw new RuntimeException(e);
            }
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }


}
