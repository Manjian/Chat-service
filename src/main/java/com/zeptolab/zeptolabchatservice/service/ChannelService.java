package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.JoinEvent;
import com.zeptolab.zeptolabchatservice.repositories.repo.ChannelRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

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
        final Optional<Channel> channelOptional = channelRepository.getChannelByName(data.channel());
        if (channelOptional.isPresent()) {
            if (channelOptional.get().getUsers().size() < 10) {
                channelOptional.get().addUser(user);
                return channelRepository.save(channelOptional.get());
            } else {
                throw new IllegalAccessException("user can't join to the target channelOptional");
            }
        } else {

            Channel channel = createChannel(data);
            channel.addUser(user);
            return updateChannel(channel);
        }


    }

    private Channel updateChannel(final Channel channel) {
        return channelRepository.save(channel);
    }

    private Channel createChannel(final JoinEvent data) {
        final Channel channel = new Channel(data.channel());
        return this.channelRepository.save(channel);
    }

}
