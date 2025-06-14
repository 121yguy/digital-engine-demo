package org.demo.user.exceptions;

public class IllegalDataException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Illegal data";

    public IllegalDataException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalDataException(String message) {
        super(message);
    }

}
