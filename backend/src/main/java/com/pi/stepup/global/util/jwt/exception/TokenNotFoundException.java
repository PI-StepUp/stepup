package com.pi.stepup.global.util.jwt.exception;

import com.pi.stepup.global.error.exception.TokenException;

public class TokenNotFoundException extends TokenException {

    public TokenNotFoundException(String message) {
        super(message);
    }
}
