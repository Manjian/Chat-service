package com.zeptolab.zeptolabchatservice.repositories.repo;

import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> getUserByName(String name);
    Optional<User> getUserBySessionId(String sessionId);

}
