package com.zeptolab.zeptolabchatservice.repositories;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.repo.ChannelRepository;
import com.zeptolab.zeptolabchatservice.repositories.repo.MessageRepository;
import com.zeptolab.zeptolabchatservice.repositories.type.MessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private ChannelRepository channelRepository;

    @AfterEach
    void resetTable() {
        repository.deleteAll();
    }

    @Test
    void getMessageByChannelIdTest() {
        //preparation
        final Channel channel = getSavedChannel();
        final Message message = new Message(MessageType.CLIENT, "content", "owner");
        message.setChannel(channel);

        repository.save(message);

        //source
        final List<Message> messagesByChannelId = repository.getMessagesByChannelId(channel.getId());

        //verification
        assertEquals(1, messagesByChannelId.size());
    }

    @Test
    void getMessageByChannelIdNotExistOrNull() {
        //preparation
        final List<Message> messagesByChannelId = repository.getMessagesByChannelId(UUID.randomUUID());
        final List<Message> messagesByChannelIdNull = repository.getMessagesByChannelId(null);

        //verification
        assertEquals(0, messagesByChannelId.size());
        assertEquals(0, messagesByChannelIdNull.size());
    }

    private Channel getSavedChannel() {
        final Channel channel = new Channel("channelName");
        return channelRepository.save(channel);
    }
}
