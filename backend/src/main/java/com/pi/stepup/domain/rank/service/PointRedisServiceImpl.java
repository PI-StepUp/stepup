package com.pi.stepup.domain.rank.service;

import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.domain.user.service.UserRedisService;
import java.util.concurrent.TimeUnit;
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

    @Value("${spring.redis.ttls.point}")
    private long POINT_EXPIRE_IN;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final UserRedisService userRedisService;


    @Override
    public void updatePoint(String id, Integer pointToAdd) {
        String userInfoKey = "user_info:" + id;
        String pointKey = "user:" + id + ":point";
        int userPoint;

        if (redisTemplate.hasKey(pointKey) == null || !redisTemplate.hasKey(pointKey)) {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            userPoint = user.getPoint();
        } else {
            userPoint = (int) redisTemplate.opsForValue().get(pointKey);
        }
        userPoint = userPoint + pointToAdd;
        log.debug("[DEBUG] {} 사용자 포인트 : {}", id, userPoint);

        if (redisTemplate.hasKey(userInfoKey) != null && redisTemplate.hasKey(userInfoKey)) {
            redisTemplate.opsForHash().put(userInfoKey, "point", userPoint);
        }

        redisTemplate.opsForValue().getAndSet(pointKey, userPoint);
        redisTemplate.expire(pointKey, POINT_EXPIRE_IN, TimeUnit.MILLISECONDS);
    }

    @Override
    public void updateRank(String id, RankName rankName) {
        String userInfoKey = "user_info:" + id;
        String rankKey = "user:" + id + ":rank";

        if (redisTemplate.hasKey(userInfoKey) != null && redisTemplate.hasKey(userInfoKey)) {
            redisTemplate.opsForHash().put(userInfoKey, "rankName", rankName);
        }
        redisTemplate.opsForValue().getAndSet(rankKey, rankName);
        redisTemplate.expire(rankKey, POINT_EXPIRE_IN, TimeUnit.MILLISECONDS);
    }

    @Override
    public int getPoint(String id) {
        String userInfoKey = "user_info:" + id;
        String pointKey = "user:" + id + ":point";

        if (redisTemplate.hasKey(pointKey) != null && redisTemplate.hasKey(pointKey)) {
            return (int) redisTemplate.opsForValue().get(pointKey);
        }

        if (redisTemplate.hasKey(userInfoKey) != null && redisTemplate.hasKey(userInfoKey)) {
            return userRedisService.getUserInfo(id).getPoint();
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
            USER_NOT_FOUND.getMessage()));
        redisTemplate.opsForValue().getAndSet(pointKey, user.getPoint());
        redisTemplate.expire(pointKey, POINT_EXPIRE_IN, TimeUnit.MILLISECONDS);
        return (int) redisTemplate.opsForValue().get(pointKey);
    }

    @Override
    public RankName getRankName(String id) {
        String userInfoKey = "user_info:" + id;
        String rankKey = "user:" + id + ":rank";
        if (redisTemplate.hasKey(rankKey) != null && redisTemplate.hasKey(rankKey)) {
            String rankName = (String) redisTemplate.opsForValue().get(rankKey);
            rankName = rankName.replaceAll("^\"|\"$", "").replaceAll("\\\\", "");
            return RankName.valueOf(rankName.toUpperCase());
        }

        if (redisTemplate.hasKey(userInfoKey) != null && redisTemplate.hasKey(userInfoKey)) {
            return RankName.valueOf(userRedisService.getUserInfo(id).getRankName());
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
            USER_NOT_FOUND.getMessage()));
        redisTemplate.opsForValue().getAndSet(rankKey, user.getRank().getName());
        redisTemplate.expire(rankKey, POINT_EXPIRE_IN, TimeUnit.MILLISECONDS);
        return user.getRank().getName();
    }
}
