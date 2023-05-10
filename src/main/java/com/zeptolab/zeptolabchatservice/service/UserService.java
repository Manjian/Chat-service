package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.data.LoginData;
import com.zeptolab.zeptolabchatservice.repositories.UserRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.Device;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    private void save(final User user) {
        userRepository.save(user);
    }


    public User getUserBySessionId(String sessionId){
        return userRepository.getUserBySessionId(sessionId);
    }

    public Optional<User> insertOrUpdate(final LoginData loginData,
                                         final Device device,
                                         final String sessionId) {
        final Optional<User> userOptional = this.getByName(loginData.name());

        final User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (Objects.equals(user.getPassword(), loginData.password())) {
                user.setSessionId(sessionId);
                user.addDevice(device);
                this.save(user);
                log.info(" new device has been inserted with {} user ", user.getName());
            } else {
                log.info("user login failed the pass not match");
                return Optional.empty();
            }
        } else {
            user = new User(loginData.name(), loginData.password(),sessionId);
            user.addDevice(device);
            this.save(user);
            log.info(" new user has been inserted with {} user ", user.getName());
        }
        return Optional.of(user);

    }
}
