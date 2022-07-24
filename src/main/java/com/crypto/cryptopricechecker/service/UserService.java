package com.crypto.cryptopricechecker.service;

import com.crypto.cryptopricechecker.persistence.model.User;
import com.crypto.cryptopricechecker.persistence.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String password) {
        // Hash the new user password before saving it to the DB
        // TODO: add some password and username requirements
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        var saved =
                new User(username, hashedPassword, java.util.UUID.randomUUID().toString(), false);

        userRepository.save(saved);

        return saved;
    }

    /**
     * By logging a registered user can retrieve its api token
     * @param username - the username of the registered user
     * @param password - the password of the registered user
     * @return - the api token of this user
     */
    public String login(String username, String password) {
        var user = userRepository.findById(username);

        if (user.isEmpty()) {
            throw new IllegalCallerException("Such user doesn't exist!");
        }

        // Confirm the password is correct
        BCrypt.Result result =
                BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword());

        if (result.verified) {
            return user.get().getAuthToken();
        }

        throw new IllegalCallerException("Sorry, wrong password!");
    }

    /**
     * Find wheter an user exists based on its auth token
     * @param authToken - the api token of the suer
     * @return - User if it exists
     *         - Optional.Empty if it doesn't
     */
    public Optional<User> findByAuthToken(String authToken) {
        var user = userRepository.findByAuthToken(authToken);

        if (user == null) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}
