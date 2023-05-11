package com.zeptolab.zeptolabchatservice.repositories.repo;

import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> getUserByName(String name);
    Optional<User> getUserBySessionId(String sessionId);
    List<User> getUsersByChannel_Name(String channel);

}
