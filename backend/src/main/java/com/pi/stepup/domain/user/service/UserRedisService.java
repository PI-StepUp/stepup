package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.user.domain.RefreshToken;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.domain.UserInfo;

public interface UserRedisService {

    void saveRefreshToken(String id, String refreshToken);

    RefreshToken getRefreshToken(String id);

    void deleteRefreshToken(String id);

    void saveUserInfo(User user);

    UserInfo getUserInfo(String id);

    void deleteUserInfo(User user);
}
