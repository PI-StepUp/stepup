package com.pi.stepup.global.util.jwt.exception;

import com.pi.stepup.global.error.exception.TokenException;

public class MalformedHeaderException extends TokenException {

    public MalformedHeaderException(String message) {
        super(message);
    }
}
