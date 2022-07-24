package com.crypto.cryptopricechecker.web.security;

import static com.crypto.cryptopricechecker.web.security.AuthHeaderProvider.CONTEXT_HEADER_ERROR_MESSAGE;

import com.crypto.cryptopricechecker.service.UserService;
import com.crypto.cryptopricechecker.web.model.UserRequest;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;


// TODO: Research AbstractPreAuthenticatedProcessingFilter

/**
 * A pre-authenticated filter that expects authentication header from
 * the client.
 */
@Slf4j
public class ContextHeaderPreAuthFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final AuthenticationManager authManager;


    public ContextHeaderPreAuthFilter(AuthenticationManager authManager,
                                      UserService userService) {
        this.authManager = authManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authToken = request.getHeader("AUTH-TOKEN");

        if (authToken == null || authToken.isBlank()) {
            // check for auth header
            writeForbidden(response, CONTEXT_HEADER_ERROR_MESSAGE);
            return;
        }

        // Check if the auth-token is legit - if it exists at all
        var user = userService.findByAuthToken(authToken);
        if (user.isEmpty()) {
            writeForbidden(response, "Sorry, invalid auth-token!");
            return;
        }

        // create authentication without grants => it is yet not authenticated
        // the authentication provider will try to validate the auth object
        var authRequest = new PreAuthenticatedAuthenticationToken(user.get(),
                new UserRequest(request.getRequestURI()));
        Authentication authResult = null;
        try {
            // let the spring config's auth manager
            // do the actual authentication
            authResult = this.authManager.authenticate(authRequest);
        } catch (AuthenticationException ae) {
            writeForbidden(response, ae.getMessage());
            return;
        }
        if (authResult == null || !authResult.isAuthenticated()) {
            writeForbidden(response, "Unsuccessful auth-header authentication!");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
        filterChain.doFilter(request, response);
    }

    private void writeForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(message);
    }

}