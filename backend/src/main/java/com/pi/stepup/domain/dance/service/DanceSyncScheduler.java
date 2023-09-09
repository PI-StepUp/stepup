package com.pi.stepup.domain.dance.service;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import java.time.LocalDateTime;
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
public class DanceSyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final DanceRepository danceRepository;
    private final UserRepository userRepository;

    private final long SCHEDULED_TIME = 3600000;

    @Scheduled(fixedDelay = SCHEDULED_TIME)
    @Transactional
    public void syncExpiredDataToDB() {
        Set<String> reservationRedisKeys = redisTemplate.keys("reservation:*");

        for (String reservationRedisKey : reservationRedisKeys) {
            Set<Object> set = redisTemplate.opsForSet().members(reservationRedisKey);

            String userId = getUserId(reservationRedisKey);
            User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            Long userPk = user.getUserId();

            Set<Long> randomDanceIdSet = new HashSet<>();
            for (Object value : set) {
                Long randomDanceId = Long.valueOf(String.valueOf(value));
                randomDanceIdSet.add(randomDanceId);
            }

            List<Reservation> reservationList = danceRepository.findAllMyReservation(userPk);
            for (int i = 0; i < reservationList.size(); i++) {
                Long randomDanceId = reservationList.get(i).getRandomDance().getRandomDanceId();

                if (randomDanceIdSet.contains(randomDanceId)) {
                    randomDanceIdSet.remove(randomDanceId);
                }
            }

            List<Reservation> reservationInsertList = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (Long randomDanceId : randomDanceIdSet) {
                RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                    -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

                if (randomDance.getStartAt().isAfter(now)) {
                    Reservation reservation = Reservation.builder()
                        .user(user)
                        .randomDance(randomDance)
                        .build();

                    reservationInsertList.add(reservation);
                } else if (randomDance.getStartAt().isBefore(now)) {
                    redisTemplate.opsForSet().remove(reservationRedisKey, randomDanceId);
                }
            }

            danceRepository.insertReservationList(reservationInsertList);
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

