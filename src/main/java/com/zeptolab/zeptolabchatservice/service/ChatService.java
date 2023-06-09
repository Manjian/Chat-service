package com.zeptolab.zeptolabchatservice.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.zeptolab.zeptolabchatservice.data.ChatEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class ChatService {

    private final ChannelService channelService;

    private static final String READ_MESSAGE = "read_message";

    public ChatService(final ChannelService channelService) {
        this.channelService = channelService;
    }

    public synchronized void sendSocketMessage(final SocketIOClient senderClient,
                                               final ChatEvent chatEvent) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(chatEvent.channel()).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(READ_MESSAGE, chatEvent.text());
            }
        }
    }

    @Transactional
    public synchronized void saveMessage(final SocketIOClient senderClient, final ChatEvent chatEvent) {

        final Optional<Channel> channel = channelService.getChannelByName(chatEvent.channel());
        if (channel.isEmpty()) {
            log.error(" no channel name with {} ", chatEvent.channel());
            return;
        }
        final Message message = new Message(
                chatEvent.text(),
                chatEvent.username());

        channel.get().addMessage(message);
        channelService.insertMessage(channel.get());
        sendSocketMessage(senderClient, chatEvent);
        log.info("new message has been save and send");
    }
}
