package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.repositories.repo.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@Slf4j
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public synchronized Channel joinOrCreate(final User user,
                                             final JoinEvent data) throws IllegalAccessException {
        final Optional<Channel> channelOptional = getChannelByName(data.channel());
        if (channelOptional.isPresent()) {
            final Channel channel = channelOptional.get();
            if (channel.getUsers().contains(user)){
                return channel;
            } else if (channel.getUsers().size() < 10 ) {
                channel.addUser(user);
                return channelRepository.save(channel);
            } else {
                throw new IllegalAccessException("user can't join to the target channel");
            }
        } else {
            final Channel channel = createChannel(data);
            channel.addUser(user);
            return updateChannel(channel);
        }
    }

    public synchronized void validateUserChannel(final User user,
                                                 final JoinEvent data,
                                                 final Consumer<String> consumer) {
        final Channel currentChannel = user.getChannel();
        if (currentChannel != null) {
            final UUID currentChannelId = currentChannel.getId();
            final Optional<Channel> channelOptional = this.getChannelById(currentChannelId);
            if (channelOptional.isPresent() && !channelOptional.get().getName().equals(data.channel())) {
                consumer.accept(channelOptional.get().getName());
                log.info("user left his current Channel");
            }
        }
    }

    public Optional<Channel> getChannelByName(final String name) {
        return channelRepository.getChannelByName(name);
    }

    @Transactional
    public synchronized List<String> getChannelUsers(final String channelName) {
        final Optional<Channel> channel = this.getChannelByName(channelName);
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

    public Optional<Channel> getChannelById(final UUID id) {
        return this.channelRepository.findById(id);
    }

    public void insertMessage(final Channel channel) {
        this.channelRepository.save(channel);
    }
}
