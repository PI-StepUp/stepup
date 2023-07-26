package com.pi.stepup.global.util.jwt;

import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.EXPIRED_TOKEN;
import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.INVALID_TOKEN;
import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.MALFORMED_HEADER;

import com.pi.stepup.domain.user.dto.TokenInfo;
import com.pi.stepup.global.error.exception.TokenException;
import com.pi.stepup.global.util.jwt.exception.ExpiredTokenException;
import com.pi.stepup.global.util.jwt.exception.InvalidTokenException;
import com.pi.stepup.global.util.jwt.exception.MalformedHeaderException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long THIRTY_MINUTES = 1000 * 60 * 30;
    private final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보로 AccessToken, RefreshToken 생성하는 메서드
    public TokenInfo generateToken(Collection<? extends GrantedAuthority> authorityInfo,
        String id) {
        log.debug("jwt token provide authentication : {}", authorityInfo);
        // 권한 가져옴
        String authorities = authorityInfo.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // AccessToken 생성
        Date accessTokenExpiresIn = new Date(now + THIRTY_MINUTES);
        String accessToken = Jwts.builder()
            .setSubject(id)
            .claim("auth", authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        // RefreshToken 생성
        Date refreshTokenExpiresIn = new Date(now + ONE_WEEK);
        String refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return TokenInfo.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    // JWT 토큰 복호화 -> 토큰에 들어있는 정보 꺼냄
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            // 권한 정보 없는 토큰
            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // UserDetails 객체 생성 후 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 검증 메서드
    public boolean validateToken(String token) throws TokenException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
        }

    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith("Bearer")) {
                int tokenStartIndex = 7;
                return bearerToken.substring(tokenStartIndex);
            }
            throw new MalformedHeaderException(MALFORMED_HEADER.getMessage());
        }

        return bearerToken;
    }
}
