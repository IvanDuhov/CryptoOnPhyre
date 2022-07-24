package com.crypto.cryptopricechecker.web.security;

import com.crypto.cryptopricechecker.persistence.model.User;
import com.crypto.cryptopricechecker.web.model.UserRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

@AllArgsConstructor
public class AuthHeaderProvider extends PreAuthenticatedAuthenticationProvider {

    public static final String CONTEXT_HEADER_ERROR_MESSAGE = "Invalid headers: AUTH-TOKEN";

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        if (!supports(authentication.getClass())) {
            return null;
        }

        var user = (User) authentication.getPrincipal();
        var request = (UserRequest) authentication.getCredentials();

        // Check if user has the right to access the endpoint
        // TODO: after user quota is added check whether the user has any left quota
        if (!user.getIsPremium() && request.getRequestedURL().contains("v2")) {
            throw new PreAuthenticatedCredentialsNotFoundException(
                    "Sorry, this endpoint is only for premium users");
        }

        authentication.setAuthenticated(true);
        return authentication;
    }

}
