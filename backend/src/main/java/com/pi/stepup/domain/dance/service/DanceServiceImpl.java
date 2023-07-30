package com.pi.stepup.domain.dance.service;


import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.*;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.dance.exception.*;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.*;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

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
        User host = userRepository.findById(danceCreateRequestDto.getHostId()).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceCreateRequestDto.toEntity(host);

        List<Long> danceMusicIdList = danceCreateRequestDto.getDanceMusicIdList();
        //랜플댄 등록 시 무조건 노래 등록해야 함
        if (danceMusicIdList.size() == 0) {
            throw new DanceMusicNotFoundException(DANCEMUSICLIST_IS_NEEDED.getMessage());
        } else {
            for (int i = 0; i < danceMusicIdList.size(); i++) {
                Music music = musicRepository.findOne(
                        danceMusicIdList.get(i)).orElseThrow();
                // TODO: merge 후 추가하기
                //                    danceMusicIdList.get(i)).orElseThrow(()
                //                    -> new MusicNotFoundException(.getMessage()));
                DanceMusic danceMusic = DanceMusic.createDanceMusic(music);
                randomDance.addDanceMusicAndSetThis(danceMusic);
            }
        }

        danceRepository.insert(randomDance);
    }

    @Override
    @Transactional
    public void update(DanceUpdateRequestDto danceUpdateRequestDto) {
        User host = userRepository.findById(danceUpdateRequestDto.getHostId()).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceRepository.findOne(danceUpdateRequestDto.getRandomDanceId()).orElseThrow(()
                -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));

        //본인이 개최한 랜플댄에만 접근 가능
//        if (!randomDance.getHost().getId().equals(host.getId())) {
//            throw new DanceForbiddenException(DANCE_UPDATE_FORBIDDEN.getMessage());
//        }
        randomDance.update(danceUpdateRequestDto);
    }

    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        RandomDance randomDance
                = danceRepository.findOne(randomDanceId).orElseThrow(()
                -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));

        User host = userRepository.findById(randomDance.getHost().getId()).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        //본인이 개최한 랜플댄에만 접근 가능
//        if (!randomDance.getHost().getId().equals(host.getId())) {
//            throw new DanceForbiddenException(DANCE_DELETE_FORBIDDEN.getMessage());
//        }

        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId) {
        RandomDance randomDance
                = danceRepository.findOne(randomDanceId).orElseThrow(()
                -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));

        List<MusicFindResponseDto> allDanceMusic = new ArrayList<>();

        List<DanceMusic> danceMusicList = danceRepository.findAllDanceMusic(randomDanceId);
        if (danceMusicList.size() == 0) {
            throw new DanceMusicNotFoundException(DANCEMUSICLIST_NOT_FOUND.getMessage());
        } else {
            for (int i = 0; i < danceMusicList.size(); i++) {
                Long musicId = danceMusicList.get(i).getMusic().getMusicId();
                Music music = musicRepository.findOne(
                        musicId).orElseThrow();
                // TODO: merge 후 추가하기
//                     musicId).orElseThrow(
//                    -> new MusicNotFoundException(.getMessage()));
                MusicFindResponseDto musicFindResponseDto = MusicFindResponseDto.builder()
                        .music(music).build();
                allDanceMusic.add(musicFindResponseDto);
            }
        }

        return allDanceMusic;
    }

    @Override
    public List<DanceFindResponseDto> readAllMyOpenDance(String id) {
        User host = userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        List<DanceFindResponseDto> allMyOpenDance = new ArrayList<>();

        List<RandomDance> randomDanceList = danceRepository.findAllMyOpenDance(id);
        if (randomDanceList.size() == 0) {
            throw new DanceNotFoundException(DANCE_NOT_FOUND.getMessage());
        } else {
            for (int i = 0; i < randomDanceList.size(); i++) {
                RandomDance randomDance = randomDanceList.get(i);
                DanceFindResponseDto danceFindResponseDto
                        = DanceFindResponseDto.builder().randomDance(randomDance).build();

                allMyOpenDance.add(danceFindResponseDto);
            }
        }

        return allMyOpenDance;
    }

    //수정해야 함
    @Override
    public List<DanceSearchResponseDto> readAllRandomDance(DanceSearchRequestDto danceSearchRequestDto) {
        List<DanceSearchResponseDto> allScheduledDance = new ArrayList<>();
        List<DanceSearchResponseDto> allInProgressDance = new ArrayList<>();
        List<DanceSearchResponseDto> allDance = new ArrayList<>();

        List<RandomDance> randomDanceList
                = danceRepository.findAllDance(danceSearchRequestDto.getKeyword());
        if (randomDanceList.size() == 0) {
            throw new DanceNotFoundException(DANCE_NOT_FOUND.getMessage());
        } else {
            LocalDateTime today = LocalDateTime.now();
            String progressType = "";
            for (int i = 0; i < randomDanceList.size(); i++) {
                RandomDance randomDance = randomDanceList.get(i);
                DanceSearchResponseDto danceSearchResponseDto
                        = DanceSearchResponseDto.builder().randomDance(randomDance).build();

                //시작 시간이 현재보다 이후
                if (danceSearchResponseDto.getStartAt().isAfter(today)) {
                    progressType = ProgressType.SCHEDULED.toString();
                    danceSearchResponseDto.setProgressType(progressType);
                    allScheduledDance.add(danceSearchResponseDto);
                    allDance.add(danceSearchResponseDto);

                    //시작 시간이 현재보다 이전, 끝 시간이 현재보다 이후
                } else if (danceSearchResponseDto.getStartAt().isBefore(today)
                        && danceSearchResponseDto.getEndAt().isAfter(today)) {
                    progressType = ProgressType.IN_PROGRESS.toString();
                    danceSearchResponseDto.setProgressType(progressType);
                    allInProgressDance.add(danceSearchResponseDto);
                    allDance.add(danceSearchResponseDto);
                }
            }

            for (DanceSearchResponseDto danceSearchResponseDto : allDance) {
                danceSearchResponseDto.setProgressType(ProgressType.ALL.toString());
            }

            if (danceSearchRequestDto.getProgressType().equals(ProgressType.ALL.toString())) {
                return allDance;
            } else if (danceSearchRequestDto.getProgressType().equals(ProgressType.IN_PROGRESS.toString())) {
                return allInProgressDance;
            } else if (danceSearchRequestDto.getProgressType().equals(ProgressType.SCHEDULED.toString())) {
                return allScheduledDance;
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public void createReservation(DanceReserveRequestDto danceReserveRequestDto) {
        User user = userRepository.findById(danceReserveRequestDto.getId()).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceRepository.findOne(danceReserveRequestDto.getRandomDanceId()).orElseThrow(()
                -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));

        //본인이 개최한 랜플댄에 예약하려고 할 때
        if (randomDance.getHost().getId().equals(user.getId())) {
            throw new ReservationDuplicatedException(RESERVATION_IMPOSSIBLE.getMessage());
        }

        if (danceRepository.findReservationByRandomDanceIdAndUserId
                (danceReserveRequestDto.getRandomDanceId(), user.getUserId()).isPresent()) {
            throw new ReservationDuplicatedException(RESERVATION_DUPLICATED.getMessage());
        }

        Reservation reservation
                = Reservation.builder().randomDance(randomDance).user(user).build();

        danceRepository.insertReservation(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long randomDanceId, String id) {
        Long userId = userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));

        Long reservationId
                = danceRepository.findReservationByRandomDanceIdAndUserId(randomDanceId, userId).orElseThrow(()
                -> new ReservationNotFoundException(RESERVATION_NOT_FOUND.getMessage())).getReservationId();

        danceRepository.deleteReservation(reservationId);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyReserveDance(String id) {
        Long userId = userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        List<Reservation> allMyReservation = danceRepository.findAllMyReservation(userId);
        if (allMyReservation.size() == 0) {
            throw new ReservationNotFoundException(RESERVATION_NOT_FOUND.getMessage());
        }
        for (int i = 0; i < allMyReservation.size(); i++) {
            Long randomDanceId = allMyReservation.get(i).getRandomDance().getRandomDanceId();
            RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                    -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));
            DanceFindResponseDto danceFindResponseDto
                    = DanceFindResponseDto.builder().randomDance(randomDance).build();
            allMyRandomDance.add(danceFindResponseDto);
        }

        return allMyRandomDance;
    }

    @Override
    @Transactional
    public void createAttend(DanceAttendRequestDto danceAttendRequestDto) {
        User user = userRepository.findById(danceAttendRequestDto.getId()).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance
                = danceRepository.findOne(danceAttendRequestDto.getRandomDanceId()).orElseThrow(()
                -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));

        //다시 참여버튼 누른 경우
        //이미 DB에 등록되었기에 또 DB에 등록이 되면 안 되지만 참여는 가능
        if (danceRepository.findAttendByRandomDanceIdAndUserId
                (danceAttendRequestDto.getRandomDanceId(), user.getUserId()).isPresent()) {
            throw new AttendDuplicatedException(ATTEND_DUPLICATED.getMessage());
        }

        AttendHistory attendHistory
                = AttendHistory.builder().randomDance(randomDance).user(user).build();

        danceRepository.insertAttend(attendHistory);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyAttendDance(String id) {
        Long userId = userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage())).getUserId();

        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        List<AttendHistory> allMyAttend = danceRepository.findAllMyAttend(userId);
        if (allMyAttend.size() == 0) {
            throw new AttendNotFoundException(ATTEND_NOT_FOUND.getMessage());
        } else {
            for (int i = 0; i < allMyAttend.size(); i++) {
                Long randomDanceId = allMyAttend.get(i).getRandomDance().getRandomDanceId();
                RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow(()
                        -> new DanceNotFoundException(DANCE_NOT_FOUND.getMessage()));
                DanceFindResponseDto danceFindResponseDto
                        = DanceFindResponseDto.builder().randomDance(randomDance).build();
                allMyRandomDance.add(danceFindResponseDto);
            }
        }

        return allMyRandomDance;
    }

}