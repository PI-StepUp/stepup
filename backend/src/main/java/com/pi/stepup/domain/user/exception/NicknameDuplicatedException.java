package com.pi.stepup.domain.user.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class NicknameDuplicatedException extends DuplicatedException {

    private String message;

    public NicknameDuplicatedException() {
        super();
    }

    public NicknameDuplicatedException(String message) {
        super(message);
    }
}
