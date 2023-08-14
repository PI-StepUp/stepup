package com.pi.stepup.global.error.exception;

public class DuplicatedException extends IllegalArgumentException {

    private String message;

    public DuplicatedException() {
        this.message = "중복 오류";
    }

    public DuplicatedException(String message) {
        super(message);
    }
}
