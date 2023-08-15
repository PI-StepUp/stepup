package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.Heart;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MusicApplyRedisServiceImpl implements MusicApplyRedisService {

    //    @Value("${spring.redis.ttls.heart}")
    private long HEART_EXPIRED_IN = 3_600_600;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveHeart(String userId, Long musicApplyId) {
        String key = "user:" + userId + ":heart_music_applies";
        redisTemplate.opsForSet().add(key, musicApplyId);
    }

    @Override
    public Integer getHeartStatus(String userId, Long musicApplyId) {
        String key = "user:" + userId + ":heart_music_applies";
        Boolean isHeartExist = redisTemplate.opsForSet().isMember(key, musicApplyId);
        log.info("[DEBUG] 노래 신청 좋아요 존재 여부 : {}", isHeartExist);

        if (isHeartExist == null) {
            return 1;
        }

        if (isHeartExist) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public List<Heart> getHearts(String userId) {
        return null;
    }

    @Override
    public void deleteHeart(String userId, Long musicApplyId) {
        String key = "user:" + userId + ":heart_music_applies";
        redisTemplate.opsForSet()
            .remove(key, musicApplyId);
    }
}
