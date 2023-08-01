package com.pi.stepup.domain.board.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class MeetingBadRequestException extends NotFoundException {
    public MeetingBadRequestException() {
        super();
    }

    public MeetingBadRequestException(String message) {
        super(message);
    }
}
