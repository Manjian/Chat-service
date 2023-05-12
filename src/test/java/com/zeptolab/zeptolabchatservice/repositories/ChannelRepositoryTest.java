package com.zeptolab.zeptolabchatservice.repositories;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.repo.ChannelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository repository;

    @AfterEach
    void resetTable() {
        repository.deleteAll();
    }

    @Test
    void getChannelByNameTest() {
        //preparation
        final Channel channel = new Channel("fakeChannelName");

        repository.save(channel);

        //source
        final Optional<Channel> channelByName = repository.getChannelByName(channel.getName());

        //verification
        assertTrue(channelByName.isPresent());
    }

    @Test
    void getChannelByNameNotExistOrNullTest() {
        //source
        final Optional<Channel> channelByName = repository.getChannelByName("fakeChannelName");
        final Optional<Channel> channelByNameNull = repository.getChannelByName(null);

        //verification
        assertTrue(channelByName.isEmpty());
        assertTrue(channelByNameNull.isEmpty());
    }

    @Test
    void saveWithDuplicateName() {
        //preparation
        final Channel channel = new Channel("fakeChannelName");
        final Channel duplicate = new Channel("fakeChannelName");

        repository.save(channel);

        //verification
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(duplicate));
    }
}
