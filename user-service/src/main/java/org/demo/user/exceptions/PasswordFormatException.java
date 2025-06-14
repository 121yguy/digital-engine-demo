package org.demo.user.exceptions;

public class PasswordFormatException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Password format is incorrect";

    public PasswordFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public PasswordFormatException(String message) {
        super(message);
    }

}
