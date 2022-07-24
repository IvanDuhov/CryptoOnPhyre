package com.crypto.cryptopricechecker.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {
    private String requestedURL;
    // in case the authN and authZ are moved outside of this project, here we might want to store
    // some additional info
}
