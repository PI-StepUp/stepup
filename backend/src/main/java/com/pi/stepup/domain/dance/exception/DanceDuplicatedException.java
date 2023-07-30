package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class DanceDuplicatedException extends DuplicatedException {

    public DanceDuplicatedException() {
        super();
    }

    public DanceDuplicatedException(String message) {
        super(message);
    }

}
