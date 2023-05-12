package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.LoginEvent;
import com.zeptolab.zeptolabchatservice.repositories.repo.UserRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {


    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Optional<User> getByName(final String name) {
        return userRepository.getUserByName(name);
    }

    private User save(final User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserBySessionId(String sessionId) {
        return this.userRepository.getUserBySessionId(sessionId);
    }


    @Transactional
    public synchronized Optional<User> terminateUserAccessToChannel(final User user) {
        user.setChannel(null);
        return Optional.of(this.save(user));
    }

    @Transactional
    public synchronized Optional<User> insertOrUpdate(final LoginEvent loginEvent,
                                         final Device device,
                                         final String sessionId) {
        final Optional<User> userOptional = this.getByName(loginEvent.name());

        final User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (Objects.equals(user.getPassword(), loginEvent.password())) {
                if (sessionId.equals(user.getSessionId())){
                    log.info("session id is the same {} ", sessionId);
                    return Optional.of(user);
                } else  {
                    user.setSessionId(sessionId);
                    user.addDevice(device);
                    return Optional.of(this.save(user));
                }

            } else {
                log.info("user login failed the pass not match");
                return Optional.empty();
            }
        } else {
            user = new User(loginEvent.name(), loginEvent.password(), sessionId);
            user.addDevice(device);
            return Optional.of(this.save(user));
        }

    }

    public void validateChannelAccess(final User user) {
        if (user.getChannel() == null){
            throw new IllegalStateException("No Channel for leave");
        }
    }
}
