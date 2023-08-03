package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class DanceBadRequestException extends NotFoundException {

    public DanceBadRequestException() {
        super();
    }

    public DanceBadRequestException(String message) {
        super(message);
    }
}

