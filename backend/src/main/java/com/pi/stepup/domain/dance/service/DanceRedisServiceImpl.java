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
import java.util.*;
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

        //본인이 개최한 랜플댄에 예약하려고 할 때
        if (randomDance.getHost().getId().equals(loginUserId)) {
            throw new ReservationDuplicatedException(RESERVATION_IMPOSSIBLE.getMessage());
        }

        String id = "reservation:" + loginUserId;
        boolean hasReservation = redisTemplate.hasKey(id);

        //레디스에 처음 넣는 경우만 ttl 설정(만료되기 전이면 설정x)
        if (!hasReservation) {
            redisTemplate.opsForSet().add(id, randomDanceId);
            redisTemplate.expire(id, expiration, TimeUnit.MILLISECONDS);

            //아니면 그냥 추가만 하기
        } else {
            redisTemplate.opsForSet().add(id, randomDanceId);
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
        Long isRemoved
            = redisTemplate.opsForSet().remove(id, randomDanceId);

        log.debug("isRemoved: {}", isRemoved);

        //삭제되지 않은 경우 DB에서 삭제
        if (isRemoved == 0) {
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

        //로그인 안 한 경우에도 접근 가능
        try {
            loginUserId = SecurityUtils.getLoggedInUserId();
            user = userRepository.findById(loginUserId).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            isLogin = true;
        } catch (ForbiddenException e) {
            loginUserId = "";
        }

        //검색 타입에 따른 랜플댄 목록 조회
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
        for (int i = 0; i < randomDanceList.size(); i++) {
            RandomDance randomDance = randomDanceList.get(i);

            //로그인 안 한 사용자면 예약하기 버튼 활성화 + 클릭 시 로그인 페이지로 이동
            DanceSearchResponseDto danceSearchResponseDto
                = DanceSearchResponseDto.builder()
                .randomDance(randomDance)
                .progressType(danceSearchRequestDto.getProgressType())
                .reserveStatus(0)
                .build();

            //로그인한 사용자면 예약 상태 가져오기
            if (isLogin) {

                //레디스 먼저 조회
                String id = "reservation:" + loginUserId;
                boolean hasReservation = redisTemplate.hasKey(id);
                Set<Long> randomDanceIdSet = new HashSet<>();

                //레디스에 있으면
                if (hasReservation) {
                    Set<Object> set = redisTemplate.opsForSet().members(id);

                    if (set != null) {
                        Iterator<Object> iter = set.iterator();
                        while (iter.hasNext()) {
                            randomDanceIdSet.add(Long.valueOf(String.valueOf(iter.next())));
                        }

                        log.debug("randomDanceIdSet: {}", randomDanceIdSet);

                        for (Long value : randomDanceIdSet) {
                            //이미 진행 중이거나 진행 완료된 건 레디스 및 idSet에서 제외
                            if (randomDance.getStartAt().isBefore(now)) {
                                randomDanceIdSet.remove(value);
                                redisTemplate.opsForSet().remove(id, value);
                            }
                        }

                        log.debug("[change]: randomDanceIdSet: {}", randomDanceIdSet);
                    }

                    //레디스에 없으면 DB 조회
                } else {

                    //이때 startAt과 현재 시간 비교한 것들만 가져옴
                    for (int j = 0; j < randomDanceList.size(); j++) {
                        Optional<Reservation> reservation
                            = danceRepository.findReservationByRandomDanceIdAndUserId
                            (randomDance.getRandomDanceId(), user.getUserId());

                        if (reservation.isPresent()) {
                            randomDanceIdSet.add(reservation.get().getRandomDance().getRandomDanceId());
                            redisTemplate.opsForSet().add(id, randomDance.getRandomDanceId());
                        }
                    }

                    log.debug("randomDanceIdSet: {}", randomDanceIdSet);
                }

                //조회해 온 랜플댄 목록 + 유저의 예약상태를 포함해서 반환
                //예약이 존재하는 랜플댄이면 비활성화 - 1
                if (randomDanceIdSet.contains(randomDance.getRandomDanceId())) {
                    danceSearchResponseDto
                        = DanceSearchResponseDto.builder()
                        .randomDance(randomDance)
                        .progressType(danceSearchRequestDto.getProgressType())
                        .reserveStatus(1)
                        .build();

                    log.debug("danceSearchResponseDto: {}", danceSearchResponseDto);

                    //자기가 개최한 거면 예약하기 버튼 활성화 + 예외처리
                    //예약이 존재하지 않는 랜플댄도 예약하기 버튼 활성화
                } else {
                    danceSearchResponseDto
                        = DanceSearchResponseDto.builder()
                        .randomDance(randomDance)
                        .progressType(danceSearchRequestDto.getProgressType())
                        .reserveStatus(0)
                        .build();

                    log.debug("danceSearchResponseDto: {}", danceSearchResponseDto);
                }
            }

            allDance.add(danceSearchResponseDto);
        }

        log.debug("allDance.size(): {}", allDance.size());

        return allDance;
    }

    //TODO: redis에 있는 경우 redis것만 가져옴, DB것도 같이 가져오도록 수정하기
    @Override
    public List<DanceFindResponseDto> readAllMyReserveDance() {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        Long userId = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        //레디스 먼저 조회
        String id = "reservation:" + loginUserId;
        boolean hasReservation = redisTemplate.hasKey(id);
        log.debug("hasReservation: {}", hasReservation);
        Set<Long> randomDanceIdSet = new HashSet<>();

        //레디스에 있으면
        if (hasReservation) {
            Set<Object> set = redisTemplate.opsForSet().members(id);
            log.debug("set: {}", set);

            if (set != null) {
                Iterator<Object> iter = set.iterator();
                while (iter.hasNext()) {
                    randomDanceIdSet.add(Long.valueOf(String.valueOf(iter.next())));
                }

                log.debug("randomDanceIdSet: {}", randomDanceIdSet);

                Iterator<Long> iter2 = randomDanceIdSet.iterator();
                while (iter2.hasNext()) {
                    Long num = iter2.next();
                    log.debug("iter2: {}", num);

                    RandomDance randomDance = danceRepository.findOne(num).orElseThrow(()
                            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));
                    log.debug("randomDance: {}", randomDance);

                    //이미 진행 중이거나 진행완료된 건 레디스 및 idSet에서 제외
                    if (randomDance.getStartAt().isBefore(now)) {
                        log.debug("[remove]: randomDance-startAt-Before: {}", randomDance);

                        randomDanceIdSet.remove(id);
                        redisTemplate.opsForSet().remove(id, num);
                    }
                }

                Iterator<Long> iter3 = randomDanceIdSet.iterator();
                while (iter3.hasNext()) {
                    Long num = iter3.next();
                    log.debug("iter3: {}", num);

                    RandomDance randomDance = danceRepository.findOne(num).orElseThrow(()
                            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));
                    log.debug("randomDance: {}", randomDance);

                    DanceFindResponseDto danceFindResponseDto
                            = DanceFindResponseDto.builder().randomDance(randomDance).build();

                    allMyRandomDance.add(danceFindResponseDto);
                }
            }

            //레디스에 없으면 DB 조회
        } else {

            //이때 startAt과 현재 시간 비교한 것들만 가져옴
            List<Reservation> allMyReservation
                = danceRepository.findAllMyReservation(userId);
            log.debug("allMyReservation: {}", allMyReservation);

            for (int i = 0; i < allMyReservation.size(); i++) {
                Long randomDanceId = allMyReservation.get(i).getRandomDance().getRandomDanceId();
                RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                    -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));
                log.debug("randomDance: {}", randomDance);

                DanceFindResponseDto danceFindResponseDto
                    = DanceFindResponseDto.builder().randomDance(randomDance).build();
                log.debug("danceFindResponseDto: {}", danceFindResponseDto);

                allMyRandomDance.add(danceFindResponseDto);

                //레디스에 저장
                redisTemplate.opsForSet().add(id, randomDanceId);
            }
        }

        log.debug("allMyRandomDance.size(): {}", allMyRandomDance.size());

        return allMyRandomDance;
    }
}
