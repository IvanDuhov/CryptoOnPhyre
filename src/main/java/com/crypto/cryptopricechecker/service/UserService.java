package com.crypto.cryptopricechecker.service;

import com.crypto.cryptopricechecker.persistence.model.User;
import com.crypto.cryptopricechecker.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String password) {

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        var saved = new User(username, hashedPassword, java.util.UUID.randomUUID().toString());

        userRepository.save(saved);

        return saved;
    }

    public String login(String username, String password) {
        var user = userRepository.findById(username);

        if (user.isEmpty()) {
            throw new IllegalCallerException("Such user doesn't exist!");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword());

        if (result.verified) {
            return user.get().getAuthToken();
        }

        throw new IllegalCallerException("Sorry, wrong password!");
    }

}
