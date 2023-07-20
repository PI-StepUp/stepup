package com.pi.stepup.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    public static class CheckEmailRequestDto {

        private String email;
    }
}
