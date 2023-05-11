package com.zeptolab.zeptolabchatservice.repositories.repo;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {


    Optional<Channel> getChannelByName(final String name);


}
