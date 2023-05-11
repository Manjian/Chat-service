package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.repositories.MessageRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;


    public MessageService(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }


    public Message saveMessage(final Message messageData) {
        return messageRepository.save(messageData);
    }


    public List<Message> getMessagesByChannelId(final UUID channelId) {
        return this.messageRepository.getMessagesByChannelId(channelId).orElse(Collections.emptyList());
    }
}
