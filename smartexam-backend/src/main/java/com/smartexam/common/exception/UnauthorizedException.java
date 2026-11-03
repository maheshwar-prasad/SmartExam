package com.smartexam.common.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Authentication is required.");
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}