package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.repositories.MessageRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    public Message saveMessage(final Message messageData) {
        return messageRepository.save(messageData);
    }

}
