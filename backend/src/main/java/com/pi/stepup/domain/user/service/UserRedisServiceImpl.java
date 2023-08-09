package com.pi.stepup.domain.user.service;

import static com.pi.stepup.global.util.jwt.constant.JwtExceptionMessage.TOKEN_NOTFOUND;

import com.pi.stepup.domain.user.dao.UserRedisRepository;
import com.pi.stepup.domain.user.domain.RefreshToken;
import com.pi.stepup.global.util.jwt.exception.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRedisServiceImpl implements UserRedisService {

    private final UserRedisRepository userRedisRepository;
    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    @Override
    public void saveRefreshToken(String id, String refreshToken) {
        userRedisRepository.save(RefreshToken.builder()
            .id(id)
            .refreshToken(refreshToken)
            .ttl(REFRESH_TOKEN_EXPIRED_IN)
            .build());
    }

    @Override
    public RefreshToken getRefreshToken(String id) {
        return userRedisRepository.findById(id)
            .orElseThrow(() -> new TokenNotFoundException(TOKEN_NOTFOUND.getMessage()));
    }
}
