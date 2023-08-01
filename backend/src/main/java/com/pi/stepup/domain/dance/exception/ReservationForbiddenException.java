package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.ForbiddenException;

public class ReservationForbiddenException extends ForbiddenException {

    public ReservationForbiddenException() {
        super();
    }

    public ReservationForbiddenException(String message) {
        super(message);
    }
}
