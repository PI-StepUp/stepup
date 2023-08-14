package com.pi.stepup.global.util.jwt.filter;

import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.MALFORMED_HEADER;
import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.NOT_MATCHED_TOKEN;
import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.TOKEN_NOTFOUND;
import static com.pi.stepup.global.util.jwt.filter.constant.TokenType.ACCESS;
import static com.pi.stepup.global.util.jwt.filter.constant.TokenType.REFRESH;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi.stepup.domain.user.domain.RefreshToken;
import com.pi.stepup.domain.user.dto.TokenInfo;
import com.pi.stepup.domain.user.service.UserRedisService;
import com.pi.stepup.global.dto.ResponseDto;
import com.pi.stepup.global.error.exception.TokenException;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import com.pi.stepup.global.util.jwt.exception.MalformedHeaderException;
import com.pi.stepup.global.util.jwt.exception.NotMatchedTokenException;
import com.pi.stepup.global.util.jwt.exception.TokenNotFoundException;
import com.pi.stepup.global.util.jwt.filter.dto.Token;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRedisService userRedisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final String UTF_8 = "utf-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Request Header 로부터 JWT 토큰 받아옴
            Token token = resolveToken(request);

            // 2. JwtTokenProvider.validateToken() 으로 유효성 검사 진행
            if (token != null && jwtTokenProvider.validateToken(token.getToken())) {
                if (token.getTokenType() == REFRESH) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(
                        token.getToken());

                    RefreshToken originRefreshToken = userRedisService.getRefreshToken(
                        authentication.getName());

                    if (originRefreshToken == null) {
                        throw new TokenNotFoundException(TOKEN_NOTFOUND.getMessage());
                    }

                    if (token.getToken().equals(originRefreshToken.getRefreshToken())) {
                        TokenInfo tokenInfo = reissueTokensAndSaveOnRedis(authentication);

                        makeTokenInfoResponse(response, tokenInfo);

                        return;
                    }

                    throw new NotMatchedTokenException(NOT_MATCHED_TOKEN.getMessage());
                }

                // 토큰 유효할 경우, 토큰으로부터 Authentication 객체를 받아와 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(
                    token.getToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            makeTokenExceptionResponse(response, e);
        }
    }

    private void makeTokenExceptionResponse(HttpServletResponse response, TokenException e)
        throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
            objectMapper.writeValueAsString(
                ResponseDto.create(e.getMessage())
            )
        );
    }

    private TokenInfo reissueTokensAndSaveOnRedis(Authentication authentication) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        userRedisService.saveRefreshToken(authentication.getName(),
            tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    private void makeTokenInfoResponse(HttpServletResponse response, TokenInfo tokenInfo)
        throws IOException {
        String tokenReissuedMessage = "토큰 재발급 완료";

        response.setStatus(HttpStatus.CREATED.value());
        response.setCharacterEncoding(UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
            objectMapper.writeValueAsString(
                ResponseDto.create(
                    tokenReissuedMessage,
                    tokenInfo
                )
            )
        );
    }

    public Token resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        log.debug("bearertoken : {}", token);
        if (StringUtils.hasText(token)) {
            if (token.startsWith("Bearer") && token.length() > 7) {
                int tokenStartIndex = 7;
                return Token.builder()
                    .tokenType(ACCESS)
                    .token(token.substring(tokenStartIndex))
                    .build();
            }
            throw new MalformedHeaderException(MALFORMED_HEADER.getMessage());
        }

        token = request.getHeader("refreshToken");
        if (StringUtils.hasText(token)) {
            return Token.builder()
                .tokenType(REFRESH)
                .token(token)
                .build();
        }

        return null;
    }
}
