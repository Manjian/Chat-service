package com.zeptolab.zeptolabchatservice.services;

import com.zeptolab.zeptolabchatservice.data.LoginEvent;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Channel;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import com.zeptolab.zeptolabchatservice.repositories.repo.UserRepository;
import com.zeptolab.zeptolabchatservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "application-test.properties")
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    void getByNameTest() {
        //preparation
        final User user = getUser();

        when(repository.getUserByName(user.getName())).thenReturn(Optional.of(user));

        //source
        final Optional<User> byName = service.getByName(user.getName());

        //verification
        assertTrue(byName.isPresent());
        assertEquals(user, byName.get());
    }

    @Test
    void getByNameNotFoundTest() {
        //source
        final Optional<User> byName = service.getByName("fakeUserName");

        //verification
        assertTrue(byName.isEmpty());
    }

    @Test
    void getUserBySessionIdTest() {
        //preparation
        final User user = getUser();

        when(repository.getUserBySessionId(user.getSessionId())).thenReturn(Optional.of(user));

        //source
        final Optional<User> bySessionId = service.getUserBySessionId(user.getSessionId());

        //verification
        assertTrue(bySessionId.isPresent());
        assertEquals(user, bySessionId.get());
    }

    @Test
    void getUserBySessionIdNotFoundTest() {
        //preparation
        final Optional<User> userBySessionId = service.getUserBySessionId("fakeSessionId");

        //verification
        assertTrue(userBySessionId.isEmpty());
    }

    @Test
    void terminateUserAccessToChannelTest() {
        //preparation
        final User user = getUser();
        user.setChannel(new Channel("channel"));
        final User saved = getUser();

        when(repository.save(any())).thenReturn(saved);

        //source
        final Optional<User> returned = service.terminateUserAccessToChannel(user);

        //verification
        assertTrue(returned.isPresent());
        assertEquals(saved, returned.get());
    }

    @Test
    void validateChannelAccessTest() {
        //preparation
        final User user = getUser();
        user.setChannel(new Channel("channel"));

        //verification
        assertDoesNotThrow(() -> service.validateChannelAccess(user));
    }

    @Test
    void validateChannelAccessNegativeTest() {
        //preparation
        final User user = getUser();

        //verification
        assertThrows(IllegalStateException.class, () -> service.validateChannelAccess(user));
    }

    @Test
    void insertOrUpdateNoUserFoundTest() {
        //preparation
        final LoginEvent loginEvent = new LoginEvent("name", "password");
        final Device device = new Device("address");
        final String sessionId = "sessionId";

        when(repository.save(any())).thenReturn(getUser());

        //source
        final Optional<User> user = service.insertOrUpdate(loginEvent, device, sessionId);

        //verification
        assertTrue(user.isPresent());
    }

    @Test
    void insertOrUpdateTest() {
        //preparation
        final LoginEvent loginEvent = new LoginEvent("name", "password");
        final Device device = new Device("address");
        final String sessionId = "sessionId";
        final User user = getUser();

        when(repository.getUserByName(loginEvent.name())).thenReturn(Optional.of(user));

        //source
        final Optional<User> returned = service.insertOrUpdate(loginEvent, device, sessionId);

        //verification
        assertTrue(returned.isPresent());
    }

    @Test
    void insertOrUpdateIncorrectPasswordTest() {
        //preparation
        final LoginEvent loginEvent = new LoginEvent("name", "fakePassword");
        final Device device = new Device("address");
        final String sessionId = "sessionId";
        final User user = getUser();

        when(repository.getUserByName(loginEvent.name())).thenReturn(Optional.of(user));

        //source
        final Optional<User> returned = service.insertOrUpdate(loginEvent, device, sessionId);

        //verification
        assertTrue(returned.isEmpty());
    }

    @Test
    void insertOrUpdateDifferentSessionsTest() {
        //preparation
        final LoginEvent loginEvent = new LoginEvent("name", "password");
        final Device device = new Device("address");
        final String sessionId = "newSessionId";
        final User user = getUser();
        final User toReturn = getUser();
        toReturn.setSessionId(sessionId);

        when(repository.getUserByName(loginEvent.name())).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(toReturn);

        //source
        final Optional<User> returned = service.insertOrUpdate(loginEvent, device, sessionId);

        //verification
        assertTrue(returned.isPresent());
        assertEquals(sessionId, returned.get().getSessionId());
    }

    private User getUser() {
        return new User("name", "password", "sessionId");
    }
}
