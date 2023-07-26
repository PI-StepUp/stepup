package com.pi.stepup.global.util.jwt.exception;

import com.pi.stepup.global.error.exception.TokenException;

public class NotMatchedTokenException extends TokenException {

    public NotMatchedTokenException(String message) {
        super(message);
    }
}
