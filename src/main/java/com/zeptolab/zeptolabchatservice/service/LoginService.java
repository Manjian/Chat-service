package com.zeptolab.zeptolabchatservice.service;

import com.zeptolab.zeptolabchatservice.repositories.UserRepository;
import com.zeptolab.zeptolabchatservice.repositories.persistence.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LoginService {


    private final UserRepository userRepository;

    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> getByName(final String name){
        return userRepository.getUserByName(name);
    }

    public void save(final User user) {
        userRepository.save(user);
    }
}
