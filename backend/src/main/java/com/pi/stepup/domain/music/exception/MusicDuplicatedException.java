package com.pi.stepup.domain.music.exception;

import com.pi.stepup.global.error.exception.DuplicatedException;

public class MusicDuplicatedException extends DuplicatedException {
    public MusicDuplicatedException() {
        super();
    }

    public MusicDuplicatedException(String message) {
        super(message);
    }
}
