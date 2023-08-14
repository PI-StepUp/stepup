package com.pi.stepup.domain.user.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class IdDuplicatedException extends DuplicatedException {

    public IdDuplicatedException() {
        super();
    }

    public IdDuplicatedException(String message) {
        super(message);
    }
}
