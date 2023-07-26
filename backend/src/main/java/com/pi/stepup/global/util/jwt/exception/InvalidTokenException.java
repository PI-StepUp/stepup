package com.pi.stepup.global.util.jwt.exception;

import com.pi.stepup.global.error.exception.TokenException;

public class InvalidTokenException extends TokenException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
