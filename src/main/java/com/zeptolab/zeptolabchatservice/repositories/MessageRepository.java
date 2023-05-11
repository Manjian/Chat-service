package com.zeptolab.zeptolabchatservice.repositories;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    Optional<List<Message>> getMessagesByChannelId(final UUID channelId);

}
