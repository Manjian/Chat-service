package com.zeptolab.zeptolabchatservice.services;

import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.repositories.repo.ChannelRepository;
import com.zeptolab.zeptolabchatservice.service.ChannelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class ChannelServiceTest {

    @Autowired
    private ChannelService service;

    @MockBean
    private ChannelRepository repository;

    @Test
    void joinOrCreateTest() throws IllegalAccessException {
        //preparation
        final Channel channel = getChannel();
        final User newUser = new User("newUser", "newPassword", "newSessionId");
        final JoinEvent event = new JoinEvent(channel.getName());
        final Channel updated = getChannel();
        updated.addUser(newUser);
        newUser.setChannel(updated);

        when(repository.getChannelByName(channel.getName())).thenReturn(Optional.of(channel));
        when(repository.save(any())).thenReturn(updated);

        //source
        final Channel currentChannel = service.joinOrCreate(newUser, event);

        //verification
        assertEquals(updated.getUsers(), currentChannel.getUsers());
    }

    @Test
    void joinOrCreateNotExist() throws IllegalAccessException {
        //preparation
        final User newUser = new User("newUser", "newPassword", "newSessionId");
        when(repository.save(any())).thenReturn(new Channel("fakeChannelName"));

        //source
        Channel channel = service.joinOrCreate(newUser, new JoinEvent("fakeChannelName"));

        //verification
        assertFalse(channel.getUsers().isEmpty());
        assertTrue(channel.getUsers().contains(newUser));
    }

    @Test
    void joinOrCreateContainsUser() {
        //preparation
        final Channel channel = getChannel();
        final User currentUser = new User("current", "pass", "sessionId");
        channel.addUser(currentUser);
        currentUser.setChannel(channel);

        final JoinEvent event = new JoinEvent(channel.getName());

        when(repository.getChannelByName(channel.getName())).thenReturn(Optional.of(channel));

        //verification
        assertThrows(IllegalAccessException.class, () -> service.joinOrCreate(currentUser, event));
    }

    @Test
    void joinOrCreateCountOverTest() {
        //preparation
        final Channel channel = getChannel();
        final User currentUser = new User("current", "pass", "sessionId");

        for (int i = 0; i < 10; i++) {
            final User user = new User("current", "pass", "sessionId");
            channel.addUser(user);
            user.setChannel(channel);
        }

        final JoinEvent event = new JoinEvent(channel.getName());

        when(repository.getChannelByName(channel.getName())).thenReturn(Optional.of(channel));

        //verification
        assertThrows(IllegalAccessException.class, () -> service.joinOrCreate(currentUser, event));
    }


    @Test
    void getChannelByNameTest() {
        //preparation
        final Channel channel = new Channel("channelName");

        when(repository.getChannelByName(channel.getName())).thenReturn(Optional.of(channel));

        //source
        final Optional<Channel> channelByName = service.getChannelByName(channel.getName());

        //verification
        assertTrue(channelByName.isPresent());
    }

    @Test
    void getChannelByNameNotExistOrNull() {
        //preparation
        final Optional<Channel> channelByName = service.getChannelByName("fakeChannelName");
        final Optional<Channel> channelByNameNull = service.getChannelByName(null);

        //verification
        assertTrue(channelByName.isEmpty());
        assertTrue(channelByNameNull.isEmpty());
    }

    @Test
    void getChannelUsersTest() {
        //preparation
        final Channel channel = getChannel();
        when(repository.getChannelByName(channel.getName())).thenReturn(Optional.of(channel));

        //source
        final List<String> channelUsers = service.getChannelUsers(channel.getName());

        //verification
        assertFalse(channelUsers.isEmpty());
    }

    @Test
    void getChannelUsersEmptyOrNullTest() {
        //source
        final List<String> channelUsers = service.getChannelUsers("fakeChannelName");
        final List<String> channelUsersNull = service.getChannelUsers(null);

        //verification
        assertTrue(channelUsers.isEmpty());
        assertTrue(channelUsersNull.isEmpty());
    }

    private Channel getChannel() {
        final Channel channel = new Channel("channelName");
        final User user = new User("name", "password", "sessionId");
        channel.addUser(user);
        user.setChannel(channel);
        return channel;
    }
}
