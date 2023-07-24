package com.pi.stepup.global.util.jwt;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 인증은 되었지만, 인가되지 않은 접근에 대한 예외처리 (admin)
 */

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final String UTF_8 = "utf-8";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);

        response.getWriter().write(
            objectMapper.writeValueAsString(
                ResponseDto.create(ExceptionMessage.AUTHORIZATION_FAILED.getMessage())
            )
        );
    }
}
