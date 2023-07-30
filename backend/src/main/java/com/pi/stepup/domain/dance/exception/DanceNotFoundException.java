package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class DanceNotFoundException extends NotFoundException {

    public DanceNotFoundException() {
        super();
    }

    public DanceNotFoundException(String message) {
        super(message);
    }
}

