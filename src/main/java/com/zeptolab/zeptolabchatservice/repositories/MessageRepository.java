package com.zeptolab.zeptolabchatservice.repositories;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByRoom(String room);
}
