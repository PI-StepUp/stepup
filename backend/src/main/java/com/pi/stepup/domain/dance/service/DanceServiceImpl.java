package com.pi.stepup.domain.dance.service;


import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.ATTEND_DUPLICATED;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_INVALID_MUSIC;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_INVALID_TIME;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_DUPLICATED;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_IMPOSSIBLE;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceAttendRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceReserveRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.dance.exception.AttendDuplicatedException;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.dance.exception.ReservationDuplicatedException;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

    //TODO: user <- Token에서 얻어오는 걸로 변경?
    //@RequestHeader로?
    //아니면...spring security에서 처리 후 dto에 필드 추가해서?

    @Override
    @Transactional
    public void create(DanceCreateRequestDto danceCreateRequestDto) {
        User host = userRepository.findById(danceCreateRequestDto.getHostId()).orElseThrow(()
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
                    danceMusicIdList.get(i)).orElseThrow();
                // TODO: merge 후 추가하기
                //                    danceMusicIdList.get(i)).orElseThrow(()
                //                    -> new MusicNotFoundException(.getMessage()));
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

    //TODO: 접근권한
    @Override
    @Transactional
    public void update(DanceUpdateRequestDto danceUpdateRequestDto) {
        RandomDance randomDance = danceRepository.findOne(danceUpdateRequestDto.getRandomDanceId())
            .orElseThrow(()
                -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        randomDance.update(danceUpdateRequestDto);
    }

    //TODO: 접근권한
    //랜플댄 삭제 시 삭제 사유 받아서 처리
    //진행예정 랜플댄이 있는 경우 회원 탈퇴 불가능하게
    //진행 중이면 랜플댄 못 나가게...
    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        RandomDance randomDance
            = danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId) {
        RandomDance randomDance
            = danceRepository.findOne(randomDanceId).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        List<MusicFindResponseDto> allDanceMusic = new ArrayList<>();

        List<DanceMusic> danceMusicList = danceRepository.findAllDanceMusic(randomDanceId);
        for (int i = 0; i < danceMusicList.size(); i++) {
            Long musicId = danceMusicList.get(i).getMusic().getMusicId();
            Music music = musicRepository.findOne(musicId).orElseThrow();
            // TODO: merge 후 추가하기
//                     musicId).orElseThrow(
//                    -> new MusicNotFoundException(.getMessage()));
            MusicFindResponseDto musicFindResponseDto = MusicFindResponseDto.builder()
                .music(music).build();
            allDanceMusic.add(musicFindResponseDto);
        }

        return allDanceMusic;
    }

    @Override
    public List<DanceFindResponseDto> readAllMyOpenDance(String id) {
        User host = userRepository.findById(id).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        List<DanceFindResponseDto> allMyOpenDance = new ArrayList<>();

        List<RandomDance> randomDanceList = danceRepository.findAllMyOpenDance(id);
        for (int i = 0; i < randomDanceList.size(); i++) {
            RandomDance randomDance = randomDanceList.get(i);
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();

            allMyOpenDance.add(danceFindResponseDto);
        }

        return allMyOpenDance;
    }

    //TODO: 로그인 유저랑 비교해서 예약한 상태면 예약 버튼 안 뜨고, 예약 안 했으면 예약 버튼 뜨게 -> 0/1
    @Override
    public List<DanceSearchResponseDto> readAllRandomDance(
        DanceSearchRequestDto danceSearchRequestDto) {

        List<RandomDance> randomDanceList = new ArrayList<>();

        if (danceSearchRequestDto.getProgressType().equals(ProgressType.SCHEDULED.toString())) {
            randomDanceList
                = danceRepository.findScheduledDance(danceSearchRequestDto.getKeyword());
        } else if (danceSearchRequestDto.getProgressType()
            .equals(ProgressType.IN_PROGRESS.toString())) {
            randomDanceList
                = danceRepository.findInProgressDance(danceSearchRequestDto.getKeyword());
        } else {
            randomDanceList
                = danceRepository.findAllDance(danceSearchRequestDto.getKeyword());
        }

        List<DanceSearchResponseDto> allDance = new ArrayList<>();
        for (int i = 0; i < randomDanceList.size(); i++) {
            RandomDance randomDance = randomDanceList.get(i);

            DanceSearchResponseDto danceSearchResponseDto
                = DanceSearchResponseDto.builder()
                .randomDance(randomDance)
                .progressType(danceSearchRequestDto.getProgressType())
                .build();

            allDance.add(danceSearchResponseDto);
        }

        return allDance;
    }

    @Override
    @Transactional
    public void createReservation(DanceReserveRequestDto danceReserveRequestDto) {
        User user = userRepository.findById(danceReserveRequestDto.getId()).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceRepository.findOne(danceReserveRequestDto.getRandomDanceId())
            .orElseThrow(()
                -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

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
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        danceRepository.deleteReservation(randomDanceId, userId);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyReserveDance(String id) {
        Long userId = userRepository.findById(id).orElseThrow(()
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
    public void createAttend(DanceAttendRequestDto danceAttendRequestDto) {
        User user = userRepository.findById(danceAttendRequestDto.getId()).orElseThrow(()
            -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance
            = danceRepository.findOne(danceAttendRequestDto.getRandomDanceId()).orElseThrow(()
            -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        //다시 참여버튼 누른 경우
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