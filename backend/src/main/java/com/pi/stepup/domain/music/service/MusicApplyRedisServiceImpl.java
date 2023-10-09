package com.pi.stepup.domain.music.service;

import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_NOT_FOUND;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.exception.MusicApplyNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
public class MusicApplyRedisServiceImpl implements MusicApplyRedisService {

    @Value("${spring.redis.ttls.heart}")
    private long HEART_EXPIRED_IN; // 60_000
    private final RedisTemplate<String, Object> redisTemplate;
    private final MusicApplyRepository musicApplyRepository;

    @Override
    public void saveHeart(String userId, Long musicApplyId) {
        String userKey = "user:" + userId + ":heart_music_applies";
        log.debug("[DEBUG / saveHeart] music apply id class : {}", musicApplyId.getClass());
        checkHeartInRedis(userKey, userId, musicApplyId);
        redisTemplate.expire(userKey, HEART_EXPIRED_IN, TimeUnit.MILLISECONDS);

        String musicApplyHeartCntKey = "music_apply_id:" + musicApplyId + ":heart_cnt";
        checkHeartCntInRedis(musicApplyHeartCntKey, musicApplyId);
        redisTemplate.expire(musicApplyHeartCntKey, HEART_EXPIRED_IN, TimeUnit.MILLISECONDS);
    }

    void checkHeartInRedis(String userKey, String userId, Long musicApplyId) {
        if (redisTemplate.hasKey(userKey) == null || !redisTemplate.hasKey(userKey)) {
            Optional<Heart> heart = musicApplyRepository.findHeart(userId, musicApplyId);
            if (heart.isPresent()) {
                redisTemplate.opsForSet()
                    .add(userKey, heart.get().getMusicApply().getMusicApplyId());
                return;
            }
        }
        redisTemplate.opsForSet().add(userKey, (Long) musicApplyId);
    }

    void checkHeartCntInRedis(String musicApplyHeartCntKey, Long musicApplyId) {
        if (redisTemplate.hasKey(musicApplyHeartCntKey) == null || !redisTemplate.hasKey(
            musicApplyHeartCntKey)) {
            MusicApply musicApply = musicApplyRepository.findOne(musicApplyId).orElseThrow(
                () -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()));
            redisTemplate.opsForValue()
                .getAndSet(musicApplyHeartCntKey, musicApply.getHeartCnt() + 1);
        } else {
            redisTemplate.opsForValue().increment(musicApplyHeartCntKey, 1);
        }
    }

    @Override
    public Integer getHeartStatus(String userId, Long musicApplyId) {
        String userKey = "user:" + userId + ":heart_music_applies";

        if (redisTemplate.hasKey(userKey) == null || !redisTemplate.hasKey(userKey)) {
            Optional<Heart> heart = musicApplyRepository.findHeart(userId, musicApplyId);
            if (heart.isPresent()) {
                redisTemplate.opsForSet().add(userKey, musicApplyId);
            }
            return 0;
        } else {
            Boolean isHeartExist = redisTemplate.opsForSet().isMember(userKey, musicApplyId);
            log.info("[DEBUG] {} 사용자의 {} 번 노래 신청 좋아요 존재 여부 : {}", userId, musicApplyId,
                isHeartExist);
            if (isHeartExist == null || !isHeartExist) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public Set<Object> getHearts(Long musicApplyId) {
        String musicApplyKey = "musicApply:" + musicApplyId + ":heart_user";

        // cache miss!!, 데이터베이스에 존재하는지 찾아보고 있다면 redis에 저장한다
        Boolean isMusicApplyKeyExist = redisTemplate.hasKey(musicApplyKey);
        if (isMusicApplyKeyExist == null || !isMusicApplyKeyExist) {
            getHeartsFromDB();
        }

        return redisTemplate.opsForSet().members(musicApplyKey);
    }

    @Override
    public void deleteHeart(String userId, Long musicApplyId) { // 캐시 미스 적용 x ...
        String userKey = "user:" + userId + ":heart_music_applies";
        redisTemplate.opsForSet().remove(userKey, musicApplyId);

        String musicApplyHeartCntKey = "music_apply_id:" + musicApplyId + ":heart_cnt";
        redisTemplate.opsForValue().decrement(musicApplyHeartCntKey, 1);
    }

    @Override
    public boolean checkRedisEmpty() {
        String isFirstSearch = "first_search:";
        if (redisTemplate.keys(isFirstSearch).isEmpty()) {
            redisTemplate.opsForValue().append(isFirstSearch, "false");
            redisTemplate.expire(isFirstSearch, HEART_EXPIRED_IN, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    @Override
    public void getHeartsFromDB() {
        log.info("[INFO] DB 데이터 Redis에 저장 중 ...");
        List<MusicApply> musicAppliesFromDB = musicApplyRepository.findAll("");

        for(MusicApply ma : musicAppliesFromDB) {
            String userKey = "user:" + ma.getWriter().getId() + ":heart_music_applies";
            log.debug("[DEBUG / saveHeart] music apply : {}",
                ma.getMusicApplyId());
            redisTemplate.opsForSet().add(userKey, (Long) ma.getMusicApplyId());
            redisTemplate.expire(userKey, HEART_EXPIRED_IN, TimeUnit.MILLISECONDS);

            String musicApplyHeartCntKey =
                "music_apply_id:" + ma.getMusicApplyId() + ":heart_cnt";
            redisTemplate.opsForValue()
                .getAndSet(musicApplyHeartCntKey, ma.getHeartCnt());
            redisTemplate.expire(musicApplyHeartCntKey, HEART_EXPIRED_IN, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public Integer getHeartCnt(Long musicApplyId) {
        String musicApplyHeartCntKey = "music_apply_id:" + musicApplyId + ":heart_cnt";
        if (redisTemplate.hasKey(musicApplyHeartCntKey) == null || !redisTemplate.hasKey(
            musicApplyHeartCntKey)) {
            getHeartsFromDB();
        }

        log.info("### [MusicApplyRedisService/getHeartCnt] key : {} - value : {}",
            musicApplyHeartCntKey, redisTemplate.opsForValue().get(musicApplyHeartCntKey));
        return (Integer) redisTemplate.opsForValue().get(musicApplyHeartCntKey);
    }
}
