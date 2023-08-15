package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.dao.PointHistoryRepository;
import com.pi.stepup.domain.user.service.UserRedisService;
import com.pi.stepup.domain.user.service.UserRedisServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PointRedisServiceImpl implements PointRedisService {

    private final PointHistoryRepository pointHistoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRedisService userRedisService;

    @Override
    public void updatePoint(String id, Integer pointToAdd) {
        Integer userPoint = Integer.valueOf(
            String.valueOf(userRedisService.getUserInfo(id).getPoint()));
        userPoint = userPoint + pointToAdd;

        log.debug("[DEBUG] {} 사용자 포인트 : {}", id, userPoint);

        String userInfoKey = "user_info:" + id;
        redisTemplate.opsForHash().put(userInfoKey, "point", String.valueOf(userPoint));
    }

    @Override
    public void updateRank(String id, RankName rankName) {
        String userInfoKey = "user_info:" + id;
        redisTemplate.opsForHash().put(userInfoKey, "rankName", String.valueOf(rankName));
    }
}
