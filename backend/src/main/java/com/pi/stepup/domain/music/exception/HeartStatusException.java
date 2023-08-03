package com.pi.stepup.domain.music.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class HeartStatusException extends NotFoundException {

    public HeartStatusException() {
        super();
    }

    public HeartStatusException(String message) {
        super(message);
    }
}
