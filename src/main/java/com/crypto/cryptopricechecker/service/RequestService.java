package com.crypto.cryptopricechecker.service;

import com.crypto.cryptopricechecker.persistence.model.Request;
import com.crypto.cryptopricechecker.persistence.model.User;
import com.crypto.cryptopricechecker.persistence.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestService {

    private final RequestRepository repository;

    public void saveRequest(String ticker) {
        // Retrieve the current authenticated user
        var currentUser = (User) SecurityContextHolder.getContext()
                                    .getAuthentication().getPrincipal();

        // Create the request object
        Request req = new Request();
        req.setRequestedTicker(ticker);
        req.setUsername(currentUser.getUsername());

        repository.save(req);
    }

}
