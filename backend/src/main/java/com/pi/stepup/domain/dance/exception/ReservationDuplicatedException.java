package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class ReservationDuplicatedException extends DuplicatedException {

    public ReservationDuplicatedException() {
        super();
    }

    public ReservationDuplicatedException(String message) {
        super(message);
    }

}
