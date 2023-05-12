package com.zeptolab.zeptolabchatservice.repositories.repo;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    Optional<Channel> getChannelByName(final String name);

}
