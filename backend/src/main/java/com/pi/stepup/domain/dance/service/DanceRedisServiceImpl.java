package com.pi.stepup.domain.dance.service;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_IMPOSSIBLE;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.dance.exception.ReservationDuplicatedException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import com.pi.stepup.global.error.exception.ForbiddenException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
public class DanceRedisServiceImpl implements DanceRedisService {

    private final DanceRepository danceRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.redis.ttls.reservation}")
    private Long expiration;

    @Override
    public void createReservation(Long randomDanceId) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        if (randomDance.getHost().getId().equals(loginUserId)) {
            throw new ReservationDuplicatedException(RESERVATION_IMPOSSIBLE.getMessage());
        }

        String id = "reservation:" + loginUserId;
        boolean hasReservation = redisTemplate.hasKey(id);

        redisTemplate.opsForSet().add(id, randomDanceId);

        if (!hasReservation) {
            redisTemplate.expire(id, expiration, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void deleteReservation(Long randomDanceId) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        Long userId = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        String id = "reservation:" + loginUserId;
        boolean hasReservation = redisTemplate.hasKey(id);
        if (hasReservation) {
            Long isRemoved
                = redisTemplate.opsForSet().remove(id, randomDanceId);

            if (isRemoved == 0) {
                danceRepository.deleteReservation(randomDanceId, userId);
            }

        } else {
            danceRepository.deleteReservation(randomDanceId, userId);
        }
    }

    public List<DanceSearchResponseDto> readAllRandomDance(
        DanceSearchRequestDto danceSearchRequestDto) {
        List<RandomDance> randomDanceList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        boolean isLogin = false;
        String loginUserId = "";
        User user = null;

        try {
            loginUserId = SecurityUtils.getLoggedInUserId();
            user = userRepository.findById(loginUserId).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            isLogin = true;
        } catch (ForbiddenException e) {
            loginUserId = "";
        }

        if (danceSearchRequestDto.getProgressType().equals(ProgressType.SCHEDULED.toString())) {
            randomDanceList
                = danceRepository.findScheduledDance(danceSearchRequestDto.getKeyword());
        } else if (danceSearchRequestDto.getProgressType()
            .equals(ProgressType.IN_PROGRESS.toString())) {
            randomDanceList
                = danceRepository.findInProgressDance(danceSearchRequestDto.getKeyword());
        } else if (danceSearchRequestDto.getProgressType().equals(ProgressType.ALL.toString())) {
            randomDanceList
                = danceRepository.findAllDance(danceSearchRequestDto.getKeyword());
        }

        List<DanceSearchResponseDto> allDance = new ArrayList<>();

        if (isLogin) {
            String id = "reservation:" + loginUserId;
            boolean hasReservation = redisTemplate.hasKey(id);
            Set<Long> randomDanceIdSet = new HashSet<>();

            if (hasReservation) {
                Set<Object> set = redisTemplate.opsForSet().members(id);

                if (set != null) {
                    Iterator<Object> iter = set.iterator();
                    while (iter.hasNext()) {
                        randomDanceIdSet.add(Long.valueOf(String.valueOf(iter.next())));
                    }

                    Iterator<Long> iter2 = randomDanceIdSet.iterator();
                    while (iter2.hasNext()) {
                        Long num = iter2.next();

                        RandomDance randomDance = danceRepository.findOne(num).orElseThrow(()
                            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

                        if (randomDance.getStartAt().isBefore(now)) {
                            iter2.remove();

                            redisTemplate.opsForSet().remove(id, num);
                        }
                    }
                }

            } else {
                for (int j = 0; j < randomDanceList.size(); j++) {
                    RandomDance randomDance = randomDanceList.get(j);

                    Optional<Reservation> reservation
                        = danceRepository.findReservationByRandomDanceIdAndUserId
                        (randomDance.getRandomDanceId(), user.getUserId());

                    if (reservation.isPresent()) {
                        randomDanceIdSet.add(
                            reservation.get().getRandomDance().getRandomDanceId());
                        redisTemplate.opsForSet().add(id, randomDance.getRandomDanceId());
                    }
                }

                redisTemplate.expire(id, expiration, TimeUnit.MILLISECONDS);
            }

            for (int i = 0; i < randomDanceList.size(); i++) {
                RandomDance randomDance = randomDanceList.get(i);

                DanceSearchResponseDto danceSearchResponseDto
                    = null;

                if (randomDanceIdSet.contains(randomDance.getRandomDanceId())) {
                    danceSearchResponseDto
                        = DanceSearchResponseDto.builder()
                        .randomDance(randomDance)
                        .progressType(danceSearchRequestDto.getProgressType())
                        .reserveStatus(1)
                        .build();

                } else {
                    danceSearchResponseDto
                        = DanceSearchResponseDto.builder()
                        .randomDance(randomDance)
                        .progressType(danceSearchRequestDto.getProgressType())
                        .reserveStatus(0)
                        .build();
                }

                allDance.add(danceSearchResponseDto);
            }

        } else {

            for (int i = 0; i < randomDanceList.size(); i++) {
                RandomDance randomDance = randomDanceList.get(i);

                DanceSearchResponseDto danceSearchResponseDto
                    = DanceSearchResponseDto.builder()
                    .randomDance(randomDance)
                    .progressType(danceSearchRequestDto.getProgressType())
                    .reserveStatus(0)
                    .build();

                allDance.add(danceSearchResponseDto);
            }
        }

        return allDance;
    }

    @Override
    public List<DanceFindResponseDto> readAllMyReserveDance() {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        Long userId = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        String id = "reservation:" + loginUserId;
        boolean hasReservation = redisTemplate.hasKey(id);
        Set<Long> randomDanceIdSet = new HashSet<>();

        if (hasReservation) {
            Set<Object> set = redisTemplate.opsForSet().members(id);

            if (set != null) {
                Iterator<Object> iter = set.iterator();
                while (iter.hasNext()) {
                    randomDanceIdSet.add(Long.valueOf(String.valueOf(iter.next())));
                }

                List<RandomDance> randomDanceList = new ArrayList<>();
                Iterator<Long> iter2 = randomDanceIdSet.iterator();
                while (iter2.hasNext()) {
                    Long num = iter2.next();

                    RandomDance randomDance = danceRepository.findOne(num).orElseThrow(()
                        -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

                    if (randomDance.getStartAt().isBefore(now)) {
                        iter2.remove();
                        redisTemplate.opsForSet().remove(id, num);
                    } else {
                        randomDanceList.add(randomDance);
                    }
                }

                for(int i=0; i< randomDanceList.size(); i++) {
                    RandomDance randomDance = randomDanceList.get(i);

                    DanceFindResponseDto danceFindResponseDto
                        = DanceFindResponseDto.builder().randomDance(randomDance).build();

                    allMyRandomDance.add(danceFindResponseDto);
                }
            }

        } else {
            List<Reservation> allMyReservation
                = danceRepository.findAllMyReservation(userId);

            for (int i = 0; i < allMyReservation.size(); i++) {
                Long randomDanceId = allMyReservation.get(i).getRandomDance().getRandomDanceId();
                RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                    -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

                DanceFindResponseDto danceFindResponseDto
                    = DanceFindResponseDto.builder().randomDance(randomDance).build();

                allMyRandomDance.add(danceFindResponseDto);

                redisTemplate.opsForSet().add(id, randomDanceId);

                redisTemplate.expire(id, expiration, TimeUnit.MILLISECONDS);
            }
        }

        return allMyRandomDance;
    }
}
