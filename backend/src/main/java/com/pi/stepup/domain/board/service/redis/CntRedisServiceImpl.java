package com.pi.stepup.domain.board.service.redis;

import com.pi.stepup.domain.board.dao.board.BoardRepository;
import com.pi.stepup.domain.board.dao.redis.ViewCntRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CntRedisServiceImpl implements CntRedisService {

    private final ViewCntRedisRepository viewCntRedisRepository;
    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<Long, Long> viewCounts = new ConcurrentHashMap<>();
    private final long TTL = 3_600_000L;

    @Override
    public void increaseViewCnt(Long boardId) {
        String key = "boardViewCnt::" + boardId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        String currentViewCntStr = (String) valueOperations.get(key);

        if (currentViewCntStr == null) {
            Long currentDbViewCnt = boardRepository.findOne(boardId).get().getViewCnt();
            log.info("DB에서 가져온 viewCnt: {}", currentDbViewCnt);
            currentDbViewCnt++;
            valueOperations.set(key, String.valueOf(currentDbViewCnt), Duration.ofMillis(TTL));
        } else {
            valueOperations.increment(key, 1L);
        }
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = TTL)
    public void updateDbAndViewCnt() {
        Set<String> keys = redisTemplate.keys("boardViewCnt::*");
        for (String key : keys) {
            Long boardId = Long.parseLong(key.substring("boardViewCnt::".length()));

            String viewCntStr = (String) redisTemplate.opsForValue().get(key);
            Long viewCnt = Long.parseLong(viewCntStr);

            if (viewCnt != null) {
                viewCntRedisRepository.addViewCntFromRedis(boardId, viewCnt);
                redisTemplate.delete(key);
            }
        }
    }

    @Override
    public Long getViewCntFromRedis(Long boardId) {
        String key = "boardViewCnt::" + boardId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        String currentViewCntStr = (String) valueOperations.get(key);

        log.info("redis에서 가져온 viewCnt: {}", currentViewCntStr);
        if (currentViewCntStr != null) {
            return Long.parseLong(currentViewCntStr);
        } else {
            Long currentDbViewCnt = boardRepository.findOne(boardId).get().getViewCnt();
            return currentDbViewCnt;
        }
    }
}
