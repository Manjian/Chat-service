package com.zeptolab.zeptolabchatservice.repositories;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {


//    @Query("SELECT COUNT(channel.users) from Channel channel where channel.name = :channel")
//    Optional<Integer> getChannelSizeByUsers(final String channel);


    Optional<Channel> getChannelByName(final String name);



}
