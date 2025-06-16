package org.demo.user.exception;

public class IllegalRoleException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Illegal role";

    public IllegalRoleException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalRoleException(String message) {
        super(message);
    }
}
