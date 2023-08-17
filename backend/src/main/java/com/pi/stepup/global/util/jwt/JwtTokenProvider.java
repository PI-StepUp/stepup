package com.pi.stepup.global.util.jwt;

import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.EXPIRED_TOKEN;
import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.INVALID_TOKEN;

import com.pi.stepup.domain.user.dto.TokenInfo;
import com.pi.stepup.global.error.exception.TokenException;
import com.pi.stepup.global.util.jwt.exception.ExpiredTokenException;
import com.pi.stepup.global.util.jwt.exception.InvalidTokenException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    @Value("${jwt.access-expired-in}")
    private long ACCESS_TOKEN_EXPIRED_IN;

    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRED_IN);
        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("auth", authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRED_IN);
        String refreshToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("auth", authorities)
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return TokenInfo.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        }

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) throws TokenException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
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
}
