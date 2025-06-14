package org.demo.user.exceptions;

public class PasswordIncorrectException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Password incorrect";

    public PasswordIncorrectException() {
        super(DEFAULT_MESSAGE);
    }

    public PasswordIncorrectException(String message) {
        super(message);
    }
}
