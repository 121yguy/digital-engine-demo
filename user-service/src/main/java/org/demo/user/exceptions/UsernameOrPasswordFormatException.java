package org.demo.user.exceptions;

public class UsernameOrPasswordFormatException extends RuntimeException {

    private static final String MESSAGE = "Username or password format is incorrect.";

    public UsernameOrPasswordFormatException() {
        super(MESSAGE);
    }

    public UsernameOrPasswordFormatException(String message) {
        super(message);
    }
}
