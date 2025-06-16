package org.demo.user.exception;

public class IllegalOperationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Illegal operation.";

    public IllegalOperationException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalOperationException(String message) {
        super(message);
    }

}
