package com.pi.stepup.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private final String message;
    private final T data;

    @Builder
    public ResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
