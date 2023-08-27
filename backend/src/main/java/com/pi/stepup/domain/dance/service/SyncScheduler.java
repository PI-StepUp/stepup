package com.pi.stepup.domain.dance.service;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
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
public class SyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final DanceRepository danceRepository;
    private final UserRepository userRepository;

    private final long SCHEDULED_TIME = 3600000;
    private final long CHECK_TTL_TIME = 10000;

    @Scheduled(fixedDelay = SCHEDULED_TIME)
    @Transactional
    public void syncExpiredDataToDB() {
        Set<String> reservationRedisKeys = redisTemplate.keys("reservation:*");

        for (String reservationRedisKey : reservationRedisKeys) {
            if (redisTemplate.getExpire(reservationRedisKey) <= CHECK_TTL_TIME) {
                log.info("reservationRedisKey: {}", reservationRedisKey);

                //id에 해당하는 value 가져오기
                Set<Object> set = redisTemplate.opsForSet().members(reservationRedisKey);
                log.info("set: {}", set);

                String userId = getUserId(reservationRedisKey);

                Long userPk = userRepository.findById(userId).orElseThrow(()
                    -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

                Set<Long> randomDanceIdSet = new HashSet<>();
                for (Object value : set) {
                    Long randomDanceId = Long.valueOf(String.valueOf(value));
                    randomDanceIdSet.add(randomDanceId);
                }
                log.info("randomDanceIdSet: {}", randomDanceIdSet);

                //없는 거만 insert하기
                //이미 DB에 존재하는 리스트 가져와서 있는 거 제외하기
                List<Reservation> reservationList = danceRepository.findAllMyReservation(userPk);
                for(int i=0; i<reservationList.size(); i++) {
                    Long randomDanceId = reservationList.get(i).getRandomDance().getRandomDanceId();

                    if(randomDanceIdSet.contains(randomDanceId)) {
                        log.info("[exist] randomDanceId: {}", randomDanceId);
                        randomDanceIdSet.remove(randomDanceId);
                    }
                }
                log.info("[removed] randomDanceIdSet: {}", randomDanceIdSet);

                List<Reservation> reservationInsertList = new ArrayList<>();
                for (Long randomDanceId : randomDanceIdSet) {
                    log.info("[insert] randomDanceId: {}", randomDanceId);

                    Reservation reservation = Reservation.builder()
                        .user(userRepository.findById(userId).orElseThrow(()
                            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())))
                        .randomDance(danceRepository.findOne(randomDanceId).orElseThrow(()
                            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage())))
                        .build();

                    reservationInsertList.add(reservation);
                }

                danceRepository.insertReservationList(reservationInsertList);
            }
        }
    }

    public String getUserId(String key) {
        String regexStr = "reservation:([^\\s]+)";
        Pattern regex = Pattern.compile(regexStr);
        Matcher matcher = regex.matcher(key);
        String userId;
        if (matcher.find()) {
            userId = matcher.group(1);
        } else {
            userId = null;
        }
        return userId;
    }
}

