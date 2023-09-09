package com.pi.stepup.domain.music.component;

import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.exception.MusicApplyNotFoundException;
import com.pi.stepup.domain.music.service.MusicApplyRedisService;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
public class MusicSyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MusicApplyRepository musicApplyRepository;
    private final UserRepository userRepository;
    private final MusicApplyRedisService musicApplyRedisService;

    private final long SCHEDULED_TIME = 3_000_000; // 3_000_000
//    private final long CHECK_TTL_TIME = 60;

    @Scheduled(fixedDelay = SCHEDULED_TIME) // 3_600_000 100_000
    @Transactional
    public void syncExpiredDataToDB() {
        log.info("[INFO] Redis에 저장 된 Data DB에 저장");
        checkHeart();
        checkHeartCnt();
    }

    void checkHeartCnt() {
        log.info("[INFO] 좋아요 개수 동기화 중 ...");
        Set<String> heartCntKeysWithKeyword = redisTemplate.keys("*heart_cnt*");

        for (String heartCntKey : heartCntKeysWithKeyword) {
//            if (redisTemplate.getExpire(heartCntKey) <= CHECK_TTL_TIME) {
//                log.info("[INFO] 만료되기 60초 전인 데이터 : {}", heartCntKey);
//
//            }
            Long musicApplyId = getMusicApplyId(heartCntKey);
            int heartCnt = (int) redisTemplate.opsForValue().get(heartCntKey);

            MusicApply musicApply = musicApplyRepository.findOne(musicApplyId).orElseThrow(
                () -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()));
            musicApply.setHeartCnt(heartCnt);
        }
    }

    void checkHeart() {
        log.info("[INFO] 좋아요 동기화 중 ...");

        Set<String> userKeysWithKeyword = redisTemplate.keys("*heart_music_applies*");
        List<Heart> hearts = new ArrayList<>();

        for (String userKey : userKeysWithKeyword) {
//            if (redisTemplate.getExpire(userKey) <= CHECK_TTL_TIME) {
//                log.info("[INFO] 만료되기 60초 전인 데이터 : {}", userKey);
//
//            }
            Set<Object> values = redisTemplate.opsForSet().members(userKey);
            String userId = getUserId(userKey);

            // userId에 해당하는 musicApply 전부 삭제 => heart pk가 변경되는 현상 발생..
            musicApplyRepository.deleteHeartById(userId);

            for (Object value : values) {
                Long musicApplyId = getMusicApplyId(value);
                Heart heart = Heart.builder()
                    .musicApply(musicApplyRepository.findOne(musicApplyId).orElseThrow(
                        () -> new MusicApplyNotFoundException(
                            MUSIC_APPLY_NOT_FOUND.getMessage())))
                    .user(userRepository.findById(userId).orElseThrow(
                        () -> new UserNotFoundException(USER_NOT_FOUND.getMessage())))
                    .build();

                log.debug("[DEBUG] 삽입되는 heart music apply id: {}",
                    heart.getMusicApply().getMusicApplyId());

                hearts.add(heart);
            }
            musicApplyRepository.insertHearts(hearts);
        }
    }

    public Long getMusicApplyId(Object value) {
        // Object여서 Integer로 저장되는 문제 때문에 Long으로 형변환
        Long musicApplyId = null;
        if (value instanceof Integer) {
            musicApplyId = ((Integer) value).longValue();
        } else if (value instanceof Long) {
            musicApplyId = (Long) value;
        } else if (value instanceof String) {
            String pattern = "music_apply_id:(.*?):heart_cnt";
            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher((CharSequence) value);
            if (matcher.find()) {
                musicApplyId = Long.valueOf(matcher.group(1));
            }
        }
        return musicApplyId;
    }

    public String getUserId(String userKey) {
        // 정규식으로 아이디 가져오기
        String pattern = "user:(.*?):heart_music_applies";
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
