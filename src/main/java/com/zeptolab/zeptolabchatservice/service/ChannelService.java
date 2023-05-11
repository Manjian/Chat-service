package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.repositories.ChannelRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    private final MessageService messageService;

    public ChannelService(final ChannelRepository channelRepository,
                          final MessageService messageService) {
        this.channelRepository = channelRepository;
        this.messageService = messageService;
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
            return createChannel(data);
        }


    }

    private Channel createChannel(final JoinEvent data) {
        final Channel channel = new Channel(data.getChannel());
        return this.channelRepository.save(channel);
    }

    public List<Message> addUserToChannelHistory(final Channel channel) {
        this.channelRepository.save(channel);
        return this.getChannelMessage(channel);
    }


    public List<Message> getChannelMessage(final Channel channel) {
        return messageService.getMessagesByChannelId(channel.getId());
    }
}
