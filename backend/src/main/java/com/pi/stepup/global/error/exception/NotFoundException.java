package com.pi.stepup.global.error.exception;

public class NotFoundException extends IllegalArgumentException{

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

}
