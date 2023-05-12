package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.ChatEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.repo.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;


    public MessageService(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }

    @Transactional
    public List<ChatEvent> getMessagesByChannelId(final UUID channelId) {
        final List<Message> list = this.messageRepository.getMessagesByChannelId(channelId);
        return list.stream().map(message -> new ChatEvent(message.getMessageType(),
                message.getContent(),
                message.getChannel().getName(),
                message.getMessageOwner())).toList();
    }
}
