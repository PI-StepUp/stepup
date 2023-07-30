package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException() {
        super();
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}

