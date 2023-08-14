package com.pi.stepup.domain.dance.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class AttendDuplicatedException extends DuplicatedException {

    public AttendDuplicatedException() {
        super();
    }

    public AttendDuplicatedException(String message) {
        super(message);
    }

}
