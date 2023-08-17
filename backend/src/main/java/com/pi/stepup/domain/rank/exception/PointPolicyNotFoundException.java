package com.pi.stepup.domain.rank.exception;

import com.pi.stepup.global.error.exception.NotFoundException;

public class PointPolicyNotFoundException extends NotFoundException {

    public PointPolicyNotFoundException() {
        super();
    }

    public PointPolicyNotFoundException(String message) {
        super(message);
    }

}
