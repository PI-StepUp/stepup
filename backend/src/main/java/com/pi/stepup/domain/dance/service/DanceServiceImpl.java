package com.pi.stepup.domain.dance.service;

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
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");

    @Override
    @Transactional
    public void create(DanceCreateRequestDto danceCreateRequestDto) {
        User host = userRepository.findById(danceCreateRequestDto.getHostId()).orElseThrow();
        RandomDance randomDance = danceCreateRequestDto.toEntity(host);

        List<Long> danceMusicIdList = danceCreateRequestDto.getDanceMusicIdList();
        for (int i = 0; i < danceMusicIdList.size(); i++) {
            Music music = musicRepository.findOne(danceMusicIdList.get(i)).orElseThrow();
            DanceMusic danceMusic = DanceMusic.createDanceMusic(music);
            randomDance.addDanceMusicAndSetThis(danceMusic);
        }

        danceRepository.insert(randomDance);
    }

    @Override
    public DanceFindResponseDto readOne(Long randomDanceId) {
        RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow();
        return DanceFindResponseDto.builder().randomDance(randomDance).build();
    }

    @Override
    @Transactional
    public void update(DanceUpdateRequestDto danceUpdateRequestDto) {
        RandomDance randomDance
            = danceRepository.findOne(danceUpdateRequestDto.getRandomDanceId()).orElseThrow();

        randomDance.update(danceUpdateRequestDto);
    }

    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId) {
        List<MusicFindResponseDto> allDanceMusic = new ArrayList<>();

        List<DanceMusic> danceMusicList = danceRepository.findAllDanceMusic(randomDanceId);
        for (int i = 0; i < danceMusicList.size(); i++) {
            Long musicId = danceMusicList.get(i).getMusic().getMusicId();
            Music music = musicRepository.findOne(musicId).orElseThrow();
            MusicFindResponseDto musicFindResponseDto = MusicFindResponseDto.builder()
                .music(music).build();
            allDanceMusic.add(musicFindResponseDto);
        }

        return allDanceMusic;
    }

    @Override
    public List<DanceFindResponseDto> readAllMyOpenDance(String id) {
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

    @Override
    public List<DanceFindResponseDto> readAllRandomDance(
        DanceSearchRequestDto danceSearchRequestDto) {
        List<DanceFindResponseDto> allScheduledDance = new ArrayList<>();
        List<DanceFindResponseDto> allInProgressDance = new ArrayList<>();
        List<DanceFindResponseDto> allDance = new ArrayList<>();

        List<RandomDance> randomDanceList = danceRepository.findAllDance(
            danceSearchRequestDto.getKeyword());
        LocalDateTime today = LocalDateTime.now();
        for (int i = 0; i < randomDanceList.size(); i++) {
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDanceList.get(i)).build();

            //시작 시간이 현재보다 이후
            if (danceFindResponseDto.getStartAt().isAfter(today)) {
                allScheduledDance.add(danceFindResponseDto);
                allDance.add(danceFindResponseDto);

                //시작 시간이 현재보다 이전, 끝 시간이 현재보다 이후
            } else if (danceFindResponseDto.getStartAt().isBefore(today)
                && danceFindResponseDto.getEndAt().isAfter(today)) {
                allInProgressDance.add(danceFindResponseDto);
                allDance.add(danceFindResponseDto);
            }
        }

        if (danceSearchRequestDto.getProgressType().equals(ProgressType.ALL.toString())) {
            return allDance;
        } else if (danceSearchRequestDto.getProgressType()
            .equals(ProgressType.IN_PROGRESS.toString())) {
            return allInProgressDance;
        } else if (danceSearchRequestDto.getProgressType()
            .equals(ProgressType.SCHEDULED.toString())) {
            return allScheduledDance;
        } else {
            return null;
        }
    }

    // TODO : 자기가 개최한 거에 예약 못 하도록
    @Override
    @Transactional
    public void createReservation(DanceReserveRequestDto danceReserveRequestDto) {
        User host = userRepository.findById(danceReserveRequestDto.getId()).orElseThrow();
        RandomDance randomDance = danceRepository.findOne(danceReserveRequestDto.getRandomDanceId())
            .orElseThrow();

        Reservation reservation
            = Reservation.builder().randomDance(randomDance).user(host).build();

        danceRepository.insertReservation(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long randomDanceId, String id) {
        Long userId = userRepository.findById(id).orElseThrow().getUserId();
        Long reservationId = danceRepository.findReservation(randomDanceId, userId)
            .orElseThrow().getReservationId();
        System.out.println(reservationId);
        danceRepository.deleteReservation(reservationId);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyReserveDance(String id) {
        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        Long userId = userRepository.findById(id).orElseThrow().getUserId();
        List<Reservation> allMyReservation = danceRepository.findAllMyReservation(userId);
        for (int i = 0; i < allMyReservation.size(); i++) {
            Long randomDanceId = allMyReservation.get(i).getRandomDance().getRandomDanceId();
            RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow();
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();
            allMyRandomDance.add(danceFindResponseDto);
        }

        return allMyRandomDance;
    }

    @Override
    @Transactional
    public void createAttend(DanceAttendRequestDto danceAttendRequestDto) {
        User user = userRepository.findById(danceAttendRequestDto.getId()).orElseThrow();
        RandomDance randomDance = danceRepository.findOne(danceAttendRequestDto.getRandomDanceId())
            .orElseThrow();

        AttendHistory attendHistory
            = AttendHistory.builder().randomDance(randomDance).user(user).build();

        AttendHistory createAttend = danceRepository.insertAttend(attendHistory);
    }

    @Override
    public List<DanceFindResponseDto> readAllMyAttendDance(String id) {
        List<DanceFindResponseDto> allMyRandomDance = new ArrayList<>();

        Long userId = userRepository.findById(id).orElseThrow().getUserId();
        List<AttendHistory> allMyAttend = danceRepository.findAllMyAttend(userId);
        for (int i = 0; i < allMyAttend.size(); i++) {
            Long randomDanceId = allMyAttend.get(i).getRandomDance().getRandomDanceId();
            RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow();
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();
            allMyRandomDance.add(danceFindResponseDto);
        }

        return allMyRandomDance;
    }

}