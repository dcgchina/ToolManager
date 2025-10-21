package com.example.demojwt.service;

import com.example.demojwt.model.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class LoginService {
    private final List<User> users = Arrays.asList(
            new User("admin","$2a$12$w2SuTZK9rpYv8/qSyt62GuHynfhr8EIpnYKNrmkdjJ6DdHNJQVOYC")
    );

    public User findByUserName(String username){
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
