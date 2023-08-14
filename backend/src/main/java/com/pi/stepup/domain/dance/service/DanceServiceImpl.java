package com.pi.stepup.domain.dance.service;


import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.ATTEND_DUPLICATED;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_DELETE_FORBIDDEN;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_INVALID_MUSIC;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_INVALID_TIME;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_UPDATE_FORBIDDEN;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_DELETE_FORBIDDEN;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_DUPLICATED;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_IMPOSSIBLE;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.dance.exception.AttendDuplicatedException;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.dance.exception.DanceForbiddenException;
import com.pi.stepup.domain.dance.exception.ReservationDuplicatedException;
import com.pi.stepup.domain.dance.exception.ReservationForbiddenException;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicNotFoundException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import com.pi.stepup.global.error.exception.ForbiddenException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DanceServiceImpl implements DanceService {

    private final DanceRepository danceRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    @Override
    @Transactional
    public void create(DanceCreateRequestDto danceCreateRequestDto) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        User host = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceCreateRequestDto.toEntity(host);
        //시간이 잘못된 경우
        if (!validationDance(randomDance)) {
            throw new DanceBadRequestException(DANCE_INVALID_TIME.getMessage());
        }

        //노래는 최소 10곡 ~ 최대 50곡
        List<Long> danceMusicIdList = danceCreateRequestDto.getDanceMusicIdList();
        //지금은 일단 2곡 이상으로 해놓았음
        if (danceMusicIdList.size() >= 2 && danceMusicIdList.size() <= 50) {
            for (int i = 0; i < danceMusicIdList.size(); i++) {
                Music music = musicRepository.findOne(
                    danceMusicIdList.get(i)).orElseThrow(()
                    -> new MusicNotFoundException(MUSIC_NOT_FOUND.getMessage()));
                DanceMusic danceMusic = DanceMusic.createDanceMusic(music);
                randomDance.addDanceMusicAndSetThis(danceMusic);
            }
        } else {
            throw new DanceBadRequestException(DANCE_INVALID_MUSIC.getMessage());
        }

        danceRepository.insert(randomDance);
    }

    private boolean validationDance(RandomDance randomDance) {
        //끝 시간이 시작 시간보다 이후
        if (randomDance.getEndAt().isAfter(randomDance.getStartAt())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void update(DanceUpdateRequestDto danceUpdateRequestDto) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        String HostId = danceUpdateRequestDto.getHostId();
        if (!loginUserId.equals(HostId)) {
            throw new DanceForbiddenException(DANCE_UPDATE_FORBIDDEN.getMessage());
        }

        RandomDance randomDance = danceRepository.findOne(danceUpdateRequestDto.getRandomDanceId())
            .orElseThrow(()
                -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        randomDance.update(danceUpdateRequestDto);
    }

    //랜플댄 삭제 시 삭제 사유 받아서 처리
    //진행예정 랜플댄이 있는 경우 회원 탈퇴 불가능하게
    //진행 중이면 랜플댄 못 나가게...
    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        RandomDance randomDance
            = danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        String loginUserId = SecurityUtils.getLoggedInUserId();
        userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        String HostId = randomDance.getHost().getId();
        if (!loginUserId.equals(HostId)) {
            throw new DanceForbiddenException(DANCE_DELETE_FORBIDDEN.getMessage());
        }

        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId) {
        danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        List<MusicFindResponseDto> allDanceMusic = new ArrayList<>();

        List<DanceMusic> danceMusicList = danceRepository.findAllDanceMusic(randomDanceId);
        for (int i = 0; i < danceMusicList.size(); i++) {
            Long musicId = danceMusicList.get(i).getMusic().getMusicId();
            Music music = musicRepository.findOne(musicId).orElseThrow(()
                -> new MusicNotFoundException(MUSIC_NOT_FOUND.getMessage()));
            MusicFindResponseDto musicFindResponseDto = MusicFindResponseDto.builder()
                .music(music).build();
            allDanceMusic.add(musicFindResponseDto);
        }

        return allDanceMusic;
    }

    @Override
    public List<DanceFindResponseDto> readAllMyOpenDance() {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        List<DanceFindResponseDto> allMyOpenDance = new ArrayList<>();

        List<RandomDance> randomDanceList = danceRepository.findAllMyOpenDance(loginUserId);
        for (int i = 0; i < randomDanceList.size(); i++) {
            RandomDance randomDance = randomDanceList.get(i);
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();

            allMyOpenDance.add(danceFindResponseDto);
        }

        return allMyOpenDance;
    }

    //TODO: 수정 필요
    @Override
    public List<DanceSearchResponseDto> readAllRandomDance(
        DanceSearchRequestDto danceSearchRequestDto) {
        List<RandomDance> randomDanceList = new ArrayList<>();

        boolean isLogin = false;
        String loginUserId = "id";
        User user = null;

        //로그인 안 한 경우에도 접근 가능
        try {
            loginUserId = SecurityUtils.getLoggedInUserId();
            user = userRepository.findById(loginUserId).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
            isLogin = true;
        } catch (ForbiddenException e) {
            loginUserId = "id";
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
        for (int i = 0; i < randomDanceList.size(); i++) {
            RandomDance randomDance = randomDanceList.get(i);

            //로그인 안 한 사용자면 예약하기 버튼 비활성화 - 1
            DanceSearchResponseDto danceSearchResponseDto
                = DanceSearchResponseDto.builder()
                .randomDance(randomDance)
                .progressType(danceSearchRequestDto.getProgressType())
                .reserveStatus(0)
                .build();

            if (isLogin) {
                //자기가 개최한 거면 예약하기 버튼 비활성화 - 1
                if (randomDance.getHost().getId().equals(loginUserId)) {
                    danceSearchResponseDto
                        = DanceSearchResponseDto.builder()
                        .randomDance(randomDance)
                        .progressType(danceSearchRequestDto.getProgressType())
                        .reserveStatus(1)
                        .build();

                    //자기가 개최한 게 아니면 예약 현황 가져오기
                } else {
                    Optional<Reservation> reservation
                        = danceRepository.findReservationByRandomDanceIdAndUserId
                        (randomDance.getRandomDanceId(), user.getUserId());

                    //예약이 존재하면 예약하기 버튼 비활성화 - 1
                    if (reservation.isPresent()) {
                        danceSearchResponseDto
                            = DanceSearchResponseDto.builder()
                            .randomDance(randomDance)
                            .progressType(danceSearchRequestDto.getProgressType())
                            .reserveStatus(1)
                            .build();

                        //예약이 존재하지 않으면 예약하기 버튼 활성화 - 0
                    } else {
                        danceSearchResponseDto
                            = DanceSearchResponseDto.builder()
                            .randomDance(randomDance)
                            .progressType(danceSearchRequestDto.getProgressType())
                            .reserveStatus(0)
                            .build();
                    }
                }
            }

            allDance.add(danceSearchResponseDto);
        }

        return allDance;
    }

    @Override
    @Transactional
    public void createReservation(Long randomDanceId) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        User user = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        //본인이 개최한 랜플댄에 예약하려고 할 때
        if (randomDance.getHost().getId().equals(loginUserId)) {
            throw new ReservationDuplicatedException(RESERVATION_IMPOSSIBLE.getMessage());
        }

        if (danceRepository.findReservationByRandomDanceIdAndUserId
            (randomDanceId, user.getUserId()).isPresent()) {
            throw new ReservationDuplicatedException(RESERVATION_DUPLICATED.getMessage());
        }

        Reservation reservation
            = Reservation.builder().randomDance(randomDance).user(user).build();

        danceRepository.insertReservation(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long randomDanceId) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        Long userId = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        Reservation findRes1
            = danceRepository.findReservationByRandomDanceIdAndUserId(
            randomDanceId, userId).orElseThrow();
        Reservation findRes2
            = danceRepository.findReservationByReservationIdAndRandomDanceId(
            findRes1.getReservationId(), randomDanceId).orElseThrow();

        if (!loginUserId.equals(findRes2.getUser().getId())) {
            throw new ReservationForbiddenException(RESERVATION_DELETE_FORBIDDEN.getMessage());
        }

        danceRepository.deleteReservation(randomDanceId, userId);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyReserveDance() {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        Long userId = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        List<Reservation> allMyReservation = danceRepository.findAllMyReservation(userId);
        for (int i = 0; i < allMyReservation.size(); i++) {
            Long randomDanceId = allMyReservation.get(i).getRandomDance().getRandomDanceId();
            RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();

            allMyRandomDance.add(danceFindResponseDto);
        }

        return allMyRandomDance;
    }

    @Override
    @Transactional
    public void createAttend(Long randomDanceId) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        User user = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance
            = danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        //다시 참여버튼 누른 경우
        if (danceRepository.findAttendByRandomDanceIdAndUserId
            (randomDanceId, user.getUserId()).isPresent()) {
            throw new AttendDuplicatedException(ATTEND_DUPLICATED.getMessage());
        }

        AttendHistory attendHistory
            = AttendHistory.builder().randomDance(randomDance).user(user).build();

        danceRepository.insertAttend(attendHistory);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyAttendDance() {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        Long userId = userRepository.findById(loginUserId).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        List<AttendHistory> allMyAttend = danceRepository.findAllMyAttend(userId);
        for (int i = 0; i < allMyAttend.size(); i++) {
            Long randomDanceId = allMyAttend.get(i).getRandomDance().getRandomDanceId();
            RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();

            allMyRandomDance.add(danceFindResponseDto);
        }

        return allMyRandomDance;
    }
}