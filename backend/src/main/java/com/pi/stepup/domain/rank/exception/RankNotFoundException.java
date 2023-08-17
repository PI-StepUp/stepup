package com.pi.stepup.domain.rank.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class RankNotFoundException extends NotFoundException {

    public RankNotFoundException() {
        super();
    }

    public RankNotFoundException(String message) {
        super(message);
    }

}
