package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.user.domain.RefreshToken;

public interface UserRedisService {
    void saveRefreshToken(String id, String refreshToken);
    RefreshToken getRefreshToken(String id);
}
