package com.pi.stepup.domain.user.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class NicknameDuplicatedException extends DuplicatedException {

    public NicknameDuplicatedException() {
        super();
    }

    public NicknameDuplicatedException(String message) {
        super(message);
    }
}
