package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.ForbiddenException;

public class AttendForbiddenException extends ForbiddenException {

    public AttendForbiddenException() {
        super();
    }

    public AttendForbiddenException(String message) {
        super(message);
    }
}
