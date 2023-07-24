package com.pi.stepup.global.util.jwt.filter;

import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // 1. Request Header 로부터 JWT 토큰 받아옴
        String token = resolveToken((HttpServletRequest) request);

        // 2. JwtTokenProvider.validateToken() 으로 유효성 검사 진행
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰 유효할 경우, 토큰으로부터 Authentication 객체를 받아와 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            int tokenStartIndex = 7;
            return bearerToken.substring(tokenStartIndex);
        }
        return null;
    }
}
