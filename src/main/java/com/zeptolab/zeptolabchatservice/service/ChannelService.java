package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.repositories.ChannelRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    public Channel joinOrCreate(final User user, @NotNull final JoinEvent data) throws IllegalAccessException {
        final Optional<Channel> channel = channelRepository.getChannelByName(data.getChannel());
        if (channel.isPresent()) {
            if (channel.get().getUsers().size() < 10) {
                channel.get().addUser(user);
                return channelRepository.save(channel.get());
            } else {
                throw new IllegalAccessException("user can't join to the target channel");
            }
        } else {
            return createChannel(user, data);
        }


    }

    private Channel createChannel(final User user, final JoinEvent data) {
        final Channel channel = new Channel(data.getChannel());
        channel.addUser(user);
        return this.channelRepository.save(channel);
    }
}
