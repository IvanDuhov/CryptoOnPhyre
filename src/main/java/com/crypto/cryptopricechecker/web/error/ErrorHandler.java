package com.crypto.cryptopricechecker.web.error;

import java.net.HttpRetryException;
import java.nio.channels.IllegalSelectorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    /**
     * Handles IllegalArgumentException.
     *
     * @param e object from IllegalArgumentException class
     * @return returns HTTP 400 and the message of the exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HTTPRetryException.
     *
     * @param e object from HTTPRetryException class
     * @return returns HTTP 429 and the message of the exception
     */
    @ExceptionHandler(HttpRetryException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(HttpRetryException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * Handles IllegalSelectorException.
     *
     * @param e object from HTTPRetryException class
     * @return returns HTTP 400 and the message of the exception
     */
    @ExceptionHandler(IllegalSelectorException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalSelectorException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Sorry, the ticker you request info about isn't in the top 200.",
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalCallerException.
     *
     * @param e object from HTTPRetryException class
     * @return returns HTTP 400 and the message of the exception
     */
    @ExceptionHandler(IllegalCallerException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalCallerException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

}
