package com.pi.stepup.global.error.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {

    public TokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenException(String message) {
        super(message);
    }
}
