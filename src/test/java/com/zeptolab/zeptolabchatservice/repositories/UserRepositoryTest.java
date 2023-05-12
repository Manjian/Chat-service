package com.zeptolab.zeptolabchatservice.repositories;

import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.repositories.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @AfterEach
    void resetTable() {
        repository.deleteAll();
    }

    @Test
    void getUserByNameTest() {
        //preparation
        final User user = getUser();

        repository.save(user);

        //source
        final Optional<User> userByName = repository.getUserByName(user.getName());

        //verification
        assertTrue(userByName.isPresent());
    }

    @Test
    void getUserByNameNotExistOrNullTest() {
        //source
        final Optional<User> userByName = repository.getUserByName("fakeUserName");
        final Optional<User> userByNameNull = repository.getUserByName(null);

        //verification
        assertTrue(userByName.isEmpty());
        assertTrue(userByNameNull.isEmpty());
    }

    @Test
    void saveUserWithDuplicateName() {
        //preparation
        final User user = getUser();
        final User duplicate = getUser();

        repository.save(user);

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(duplicate));
    }

    @Test
    void getUserBySessionIdTest() {
        //preparation
        final User user = getUser();
        repository.save(user);

        //source
        final Optional<User> userBySessionId = repository.getUserBySessionId(user.getSessionId());

        //verification
        assertTrue(userBySessionId.isPresent());
    }

    @Test
    void getUserBySessionIdNotExistOrNullTest() {
        //preparation
        final Optional<User> userBySessionId = repository.getUserBySessionId("fakeSessionId");
        final Optional<User> userBySessionIdNull = repository.getUserBySessionId(null);

        //verification
        assertTrue(userBySessionId.isEmpty());
        assertTrue(userBySessionIdNull.isEmpty());
    }

    private User getUser() {
        return new User("name", "password", "sessionId");
    }
}
