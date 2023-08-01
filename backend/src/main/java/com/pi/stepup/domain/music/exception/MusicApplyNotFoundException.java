package com.pi.stepup.domain.music.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class MusicApplyNotFoundException extends NotFoundException {
    public MusicApplyNotFoundException() {
        super();
    }

    public MusicApplyNotFoundException(String message) {
        super(message);
    }
}
