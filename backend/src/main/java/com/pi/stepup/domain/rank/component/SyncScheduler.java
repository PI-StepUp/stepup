package com.pi.stepup.domain.rank.component;

import static com.pi.stepup.domain.rank.constant.RankExceptionMessage.RANK_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.exception.RankNotFoundException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    private final long SCHEDULED_TIME = 3_000_000;
    private final long CHECK_TTL_TIME = 60;

    @Scheduled(fixedDelay = SCHEDULED_TIME) // 3_600_000 10_000
    @Transactional
    public void syncExpiredDataToDB() {
        log.info("[INFO] Redis에 저장 된 Point 관련 Data DB에 저장");
        checkPoint();
        checkRank();
    }

    void checkPoint() {
        log.info("[INFO] 포인트 동기화 중 ...");
        Set<String> pointKeyWithKeywords = redisTemplate.keys("*point*");

        for (String pointKey : pointKeyWithKeywords) {
            String userId = getUserId(pointKey, "point");
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            user.updatePoint((Integer) redisTemplate.opsForValue().get(pointKey));
            redisTemplate.delete(pointKey);
        }
    }

    void checkRank() {
        log.info("[INFO] 등급 동기화 중 ...");
        Set<String> rankKeyWtihKeywords = redisTemplate.keys("*rank*");

        for (String rankKey : rankKeyWtihKeywords) {
            String userId = getUserId(rankKey, "rank");
            log.debug("[DEBUG] {} 사용자 등급 동기화 중...", userId);
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            user.setRank(rankRepository.getRankByName(
                    RankName.valueOf((String) redisTemplate.opsForValue().get(rankKey)))
                .orElseThrow(() -> new RankNotFoundException(RANK_NOT_FOUND.getMessage())));
            redisTemplate.delete(rankKey);
        }
    }

    public String getUserId(String userKey, String keyword) {
        // 정규식으로 아이디 가져오기
        String pattern = "user:(.*?):" + keyword;
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(userKey);
        String userId;
        if (matcher.find()) {
            userId = matcher.group(1);
        } else {
            userId = null;
        }
        return userId;
    }
}
