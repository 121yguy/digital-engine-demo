package org.demo.user.exception;

public class EmailOrPhoneFormatException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Email or phone format is incorrect";

    public EmailOrPhoneFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailOrPhoneFormatException(String message) {
        super(message);
    }

}
