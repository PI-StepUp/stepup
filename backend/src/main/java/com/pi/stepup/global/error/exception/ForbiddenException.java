package com.pi.stepup.global.error.exception;

public class ForbiddenException extends SecurityException {
    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
