package com.zeptolab.zeptolabchatservice.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.zeptolab.zeptolabchatservice.data.Message;
import com.zeptolab.zeptolabchatservice.data.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocketService {

    public void sendMessage(final String room,
                            final String eventName,
                            final SocketIOClient senderClient,
                            final String message) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Message(MessageType.SERVER, message));
            }
        }
    }

}