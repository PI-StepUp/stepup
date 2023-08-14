package com.pi.stepup.global.util.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi.stepup.global.dto.ResponseDto;
import com.pi.stepup.global.error.exception.TokenException;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final String UTF_8 = "utf-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Request Header 로부터 JWT 토큰 받아옴
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

            // 2. JwtTokenProvider.validateToken() 으로 유효성 검사 진행
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰 유효할 경우, 토큰으로부터 Authentication 객체를 받아와 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding(UTF_8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.getWriter().write(
                objectMapper.writeValueAsString(
                    ResponseDto.create(e.getMessage())
                )
            );
        }
    }
}
