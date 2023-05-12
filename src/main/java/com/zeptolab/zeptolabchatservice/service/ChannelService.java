package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.repositories.repo.ChannelRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    @Transactional
    public synchronized Channel joinOrCreate(final User user, @NotNull final JoinEvent data) throws IllegalAccessException {

        final Optional<Channel> channelOptional = getChannelIdByName(data.channel());

        if (channelOptional.isPresent()) {
            if (channelOptional.get().getUsers().contains(user)) {
                throw new IllegalArgumentException("User already joined to this channel");
            } else if (channelOptional.get().getUsers().size() < 10) {
                channelOptional.get().addUser(user);
                return channelRepository.save(channelOptional.get());
            } else {
                throw new IllegalAccessException("user can't join to the target channel");
            }
        } else {
            final Channel channel = createChannel(data);
            channel.addUser(user);
            return updateChannel(channel);
        }


    }

    public Optional<Channel> getChannelIdByName(final String name) {
        return channelRepository.getChannelByName(name);
    }

    @Transactional
    public List<String> getChannelUsers(final String channelName){
        final Optional<Channel> channel = this.getChannelIdByName(channelName);
        return channel.map(value -> value.getUsers()
                .stream()
                .map(User::getName)
                .toList())
                .orElseGet(Collections::emptyList);
    }

    private Channel updateChannel(final Channel channel) {
        return channelRepository.save(channel);
    }

    private Channel createChannel(final JoinEvent data) {
        final Channel channel = new Channel(data.channel());
        return this.channelRepository.save(channel);
    }

    public List<String> getAllChannel() {
        return this.channelRepository.findAll().stream().map(Channel::getName).toList();
    }

    public Optional<Channel> getChannelById(UUID id) {
        return this.channelRepository.findById(id);
    }

    public void insertMessage(final Channel channel) {
        this.channelRepository.save(channel);
    }
}
