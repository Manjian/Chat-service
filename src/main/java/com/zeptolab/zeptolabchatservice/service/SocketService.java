package com.zeptolab.zeptolabchatservice.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {


    private final MessageService messageService;


    public void sendSocketMessage(SocketIOClient senderClient, Message messageData, String room) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", messageData);
            }
        }
    }

//    public void saveMessage(SocketIOClient senderClient, Message messageData) {
//        Message storedMessageData = messageService.saveMessage(Message.builder()
//                .messageType(MessageType.CLIENT)
//                .content(messageData.getContent())
//                .room(messageData.getRoom())
//                .user(messageData.getUser())
//                .build());
//        sendSocketMessage(senderClient, storedMessageData, messageData.getRoom());
//    }

//    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
//        Message storedMessageData = messageService.saveMessage(Message.builder()
//                .messageType(MessageType.SERVER)
//                .content(message)
//                .room(room)
//                .build());
//        sendSocketMessage(senderClient, storedMessageData, room);
//    }
}
