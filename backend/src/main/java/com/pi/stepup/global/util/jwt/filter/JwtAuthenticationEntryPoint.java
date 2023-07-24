package com.pi.stepup.global.util.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi.stepup.global.dto.ResponseDto;
import com.pi.stepup.global.error.constant.ExceptionMessage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 인증 되지 않은 접근에 대한 예외 처리 클래스
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final String UTF_8 = "utf-8";


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
            objectMapper.writeValueAsString(
                ResponseDto.create(ExceptionMessage.AUTHENTICATION_FAILED.getMessage())
            )
        );
    }
}
