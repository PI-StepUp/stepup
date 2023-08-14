package com.pi.stepup.domain.board.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class BoardNotFoundException extends NotFoundException {
    public BoardNotFoundException() {
        super();
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}
