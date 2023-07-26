package com.pi.stepup.global.util.jwt.exception;

import com.pi.stepup.global.error.exception.TokenException;

public class ExpiredTokenException extends TokenException {

    public ExpiredTokenException(String message) {
        super(message);
    }
}
