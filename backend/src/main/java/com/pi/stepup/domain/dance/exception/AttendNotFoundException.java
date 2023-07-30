package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class AttendNotFoundException extends NotFoundException {

    public AttendNotFoundException() {
        super();
    }

    public AttendNotFoundException(String message) {
        super(message);
    }
}

