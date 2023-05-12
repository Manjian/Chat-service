package com.zeptolab.zeptolabchatservice.services;

import com.zeptolab.zeptolabchatservice.data.ChatEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.repo.MessageRepository;
import com.zeptolab.zeptolabchatservice.repositories.type.MessageType;
import com.zeptolab.zeptolabchatservice.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class MessageServiceTest {

    @Autowired
    private MessageService service;

    @MockBean
    private MessageRepository repository;

    @Test
    void getMessageByChannelIdTest() {
        //preparation
        final Message message = new Message(MessageType.CLIENT, "content", "owner");
        message.setChannel(new Channel());
        final UUID channelId = UUID.randomUUID();

        when(repository.getMessagesByChannelId(channelId)).thenReturn(List.of(message));

        //source
        final List<ChatEvent> messagesByChannelId = service.getMessagesByChannelId(channelId);

        //verification
        assertFalse(messagesByChannelId.isEmpty());
    }

    @Test
    void getMessageByChannelIdNotFoundTest() {
        //source
        final List<ChatEvent> messagesByChannelId = service.getMessagesByChannelId(UUID.randomUUID());

        //verification
        assertTrue(messagesByChannelId.isEmpty());
    }
}
