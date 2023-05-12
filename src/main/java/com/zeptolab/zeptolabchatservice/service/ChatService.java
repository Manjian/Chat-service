package com.zeptolab.zeptolabchatservice.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.zeptolab.zeptolabchatservice.data.ChatEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.repo.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ChatService {

    private final MessageService messageService;

    private final ChannelService channelService;

    public ChatService(final MessageService messageService, final ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }


    public void sendSocketMessage(SocketIOClient senderClient, ChatEvent chatEvent ) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(chatEvent.channel()).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", chatEvent.text());
            }
        }
    }

    public synchronized void saveMessage(final SocketIOClient senderClient, final ChatEvent chatEvent) {

        final Optional<Channel> channel = channelService.getChannelIdByName(chatEvent.name());

        if (channel.isEmpty()) {
            log.error(" no channel name with {} ", chatEvent.name());
            return;
        }
        final Message message = new Message(
                chatEvent.messageType(),
                chatEvent.text(),
                chatEvent.name());

        channel.get().addMessage(message);
        channelService.insertMessage(channel.get());
        sendSocketMessage(senderClient, chatEvent);
        log.info("new message has been save and send");
    }

}
