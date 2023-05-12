package com.zeptolab.zeptolabchatservice.repositories.repo;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    Optional<List<Message>> getMessagesByChannelId(final UUID channelId);

}
