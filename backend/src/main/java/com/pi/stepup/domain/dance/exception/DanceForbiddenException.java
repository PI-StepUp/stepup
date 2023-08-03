package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.ForbiddenException;

public class DanceForbiddenException extends ForbiddenException {

    public DanceForbiddenException() {
        super();
    }

    public DanceForbiddenException(String message) {
        super(message);
    }
}
