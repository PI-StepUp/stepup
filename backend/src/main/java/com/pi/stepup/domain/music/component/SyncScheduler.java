package com.pi.stepup.domain.music.component;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.service.MusicApplyRedisService;
import com.pi.stepup.domain.user.dao.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final MusicApplyRepository musicApplyRepository;
    private final UserRepository userRepository;
    private final MusicApplyRedisService musicApplyRedisService;

    @Scheduled(fixedDelay = 3_500_000) // 3_600_000 10_000
    @Transactional
    public void syncExpiredDataToDB() {
        log.info("[INFO] Redis에 저장 된 Data DB에 저장");
        Set<String> userKeysWithKeyword = redisTemplate.keys("*heart_music_applies*");
        List<Heart> hearts;

        // 이부분 쿼리 여러번 안나가게 수정할 것 insert into values(,) ...
        for (String userKey : userKeysWithKeyword) {
            if (redisTemplate.getExpire(userKey) <= 60) {
                log.info("[INFO] 만료되기 60초 전인 데이터 : {}", userKey);
                Set<Object> values = redisTemplate.opsForSet().members(userKey);

                String userId = getUserId(userKey);
                List<Heart> heartFromDB = musicApplyRepository.findHeartById(userId);

                for (Object value : values) {
                    Long musicApplyId = getMusicId(value);

                    deleteHeartFromDB(heartFromDB, userId, musicApplyId); // 쿼리 여러번
                    hearts = addHeartToDB(heartFromDB, userId, musicApplyId);
                    musicApplyRepository.insertHearts(hearts);

                    String musicApplyKey = "musicApply:" + musicApplyId + ":heart_user";
                    redisTemplate.delete(musicApplyKey);
                }
                redisTemplate.delete(userKey);
            }
        }
    }

//    @Scheduled(fixedDelay = 180_000)
//    public void syncDBDataToRedis() {
//        log.info("[INFO] DB 데이터 Redis에 저장 중 ...");
//        List<Heart> heartsFromDB = musicApplyRepository.findAllHeart();
//
//        for (Heart h : heartsFromDB) {
//            musicApplyRedisService.saveHeart(h.getUser().getId(),
//                h.getMusicApply().getMusicApplyId());
//        }
//    }

    public Long getMusicId(Object value) {
        // Object여서 Integer로 저장되는 문제 때문에 Long으로 형변환
        Long musicApplyId = null;
        if (value instanceof Integer) {
            musicApplyId = ((Integer) value).longValue();
        } else if (value instanceof Long) {
            musicApplyId = (Long) value;
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


    private List<Heart> addHeartToDB(List<Heart> heartFromDB, String userId, Long musicApplyId) {
        // DB에 존재하지 않으면 DB에 저장
        List<Heart> hearts = new ArrayList<>();

        for (Heart h : heartFromDB) {
            if (h.getUser().getId().equals(userId)
                && Objects.equals(h.getMusicApply().getMusicApplyId(), musicApplyId)) {
                continue;
            }

            hearts.add(Heart.builder()
                .user(userRepository.findById(userId).get())
                .musicApply(musicApplyRepository.findOne(musicApplyId).get())
                .build());
        }
        return hearts;
    }

    public void deleteHeartFromDB(List<Heart> heartFromDB, String userId, Long musicApplyId) {
        // DB에 존재하지만, redis에 없는 경우 DB에서 삭제
        if (!heartFromDB.isEmpty()) {
            boolean isHeartInDB = heartFromDB.stream()
                .anyMatch(heart -> heart.getMusicApply().getMusicApplyId().equals(musicApplyId)
                    && heart.getUser().getId().equals(userId));

            if (!isHeartInDB) {
                musicApplyRepository.deleteHeartByIdAndMusicApplyId(userId, musicApplyId);
            }
        }
    }

}
