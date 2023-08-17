package com.pi.stepup.global.error.exception;

public class NotFoundException extends IllegalArgumentException{

    public NotFoundException() {
        super("찾을 수 없음");
    }

    public NotFoundException(String message) {
        super(message);
    }

}
