package com.pi.stepup.domain.music.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class MusicNotFoundException extends NotFoundException {
    public MusicNotFoundException() {
        super();
    }

    public MusicNotFoundException(String message) {
        super(message);
    }
}
