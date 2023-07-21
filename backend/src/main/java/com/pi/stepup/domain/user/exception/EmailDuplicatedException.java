package com.pi.stepup.domain.user.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class EmailDuplicatedException extends DuplicatedException {

    private String message;

    public EmailDuplicatedException() {
        super();
    }

    public EmailDuplicatedException(String message) {
        super(message);
    }
}
