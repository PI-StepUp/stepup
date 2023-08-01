package com.pi.stepup.global.error.exception;

import javax.naming.AuthenticationException;

public class ForbiddenException extends AuthenticationException {
    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
