package com.pi.stepup.domain.board.service.redis;

import com.pi.stepup.domain.board.dao.board.BoardRepository;
import com.pi.stepup.domain.board.dao.redis.ViewCntRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CntRedisServiceImpl implements CntRedisService {

    private final ViewCntRedisRepository viewCntRedisRepository;
    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<Long, Long> viewCounts = new ConcurrentHashMap<>();
    private final long TTL = 30_000L;

//    @Override
//    public void increaseViewCnt(Long boardId) {
//        viewCounts.compute(boardId, (key, value) -> value == null ? 1 : value + 1);
//    }

    @Override
    public void increaseViewCnt(Long boardId) {
        String key = "boardViewCnt::" + boardId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        Long currentViewCnt = valueOperations.increment(key, 1L);
        if (currentViewCnt == null) {
            valueOperations.set(key, 1L, Duration.ofMillis(TTL));
        }
    }

    @Transactional
    @Scheduled(fixedRate = TTL)
    public void updateDbAndViewCnt() {
        Set<String> keys = redisTemplate.keys("boardViewCnt::*");
        for (String key : keys) {
            Long boardId = Long.parseLong(key.substring("boardViewCnt::".length()));

            String viewCntStr = (String) redisTemplate.opsForValue().get(key);
            Long viewCnt = Long.parseLong(viewCntStr);

            if (viewCnt != null) {
                Long currentDbViewCnt = boardRepository.findOne(boardId).get().getViewCnt();
                if (currentDbViewCnt == null) {
                    currentDbViewCnt = 0L;
                }
                Long totalViewCnt = currentDbViewCnt + viewCnt;
                viewCntRedisRepository.addViewCntFromRedis(boardId, totalViewCnt);
                redisTemplate.delete(key);
            }
        }
    }
}
