package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.user.dao.RefreshTokenRedisRepository;
import com.pi.stepup.domain.user.dao.UserInfoRedisRepository;
import com.pi.stepup.domain.user.domain.RefreshToken;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRedisServiceImpl implements UserRedisService {

    private final UserInfoRedisRepository userInfoRedisRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;
    @Value("${spring.redis.ttls.user-info}")
    private long USER_INFO_EXPIRED_IN;

    @Override
    @Transactional
    public void saveRefreshToken(String id, String refreshToken) {
        refreshTokenRedisRepository.save(RefreshToken.builder()
            .id(id)
            .refreshToken(refreshToken)
            .ttl(REFRESH_TOKEN_EXPIRED_IN)
            .build());
    }

    @Override
    public RefreshToken getRefreshToken(String id) {
        return refreshTokenRedisRepository.findById(id)
            .orElse(null);
    }

    @Override
    public void deleteRefreshToken(String id) {
        refreshTokenRedisRepository.delete(
            RefreshToken.builder().id(id).build()
        );
    }

    @Override
    @Transactional
    public void saveUserInfo(User user) {
        userInfoRedisRepository.save(UserInfo.builder()
            .id(user.getId())
            .email(user.getEmail())
            .emailAlert(user.getEmailAlert())
            .countryId(user.getCountry().getCountryId())
            .countryCode(user.getCountry().getCode())
            .nickname(user.getNickname())
            .birth(user.getBirth())
            .profileImg(user.getProfileImg())
            .point(user.getPoint())
            .rankName(String.valueOf(user.getRank().getName()))
            .rankImg(user.getRank().getRankImg())
            .role(user.getRole())
            .ttl(USER_INFO_EXPIRED_IN)
            .build());
    }

    @Override
    public UserInfo getUserInfo(String id) {
        UserInfo userInfo = userInfoRedisRepository.findById(id)
            .orElse(null);

        if (userInfo != null) {
            String rankName = String.valueOf(userInfo.getRankName());
            rankName = rankName.replaceAll("^\"|\"$", "").replaceAll("\\\\", "");
            RankName rank = RankName.valueOf(rankName.toUpperCase());
            userInfo.setRankName(rank.name());
        }
        return userInfo;
    }

    @Override
    @Transactional
    public void deleteUserInfo(User user) {
        userInfoRedisRepository.delete(
            UserInfo.builder()
                .id(user.getId())
                .build()
        );
    }
}
