package com.pi.stepup.domain.music.exception;

import com.pi.stepup.global.error.exception.ForbiddenException;

public class UnauthorizedUserAccessException extends ForbiddenException {
    public UnauthorizedUserAccessException() {
        super();
    }

    public UnauthorizedUserAccessException(String message) {
        super(message);
    }
}
