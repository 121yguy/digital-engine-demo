package org.demo.user.exception;

public class UsernameAlreadyExistException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Username already exists.";

    public UsernameAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public UsernameAlreadyExistException(String message) {
        super(message);
    }

}
