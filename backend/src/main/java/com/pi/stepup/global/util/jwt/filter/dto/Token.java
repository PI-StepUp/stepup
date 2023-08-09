package com.pi.stepup.global.util.jwt.filter.dto;

import com.pi.stepup.global.util.jwt.filter.constant.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class Token {

    private final TokenType tokenType;
    private final String token;
}
