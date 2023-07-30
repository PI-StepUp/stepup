package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class DanceMusicNotFoundException extends NotFoundException {

    public DanceMusicNotFoundException() {
        super();
    }

    public DanceMusicNotFoundException(String message) {
        super(message);
    }
}

