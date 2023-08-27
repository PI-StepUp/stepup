package com.pi.stepup.domain.dance.service;


import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.exception.AttendDuplicatedException;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.dance.exception.DanceForbiddenException;
import com.pi.stepup.domain.music.dao.MusicAnswerRepository;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.domain.MusicAnswer;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicNotFoundException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.*;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_ANSWER_NOT_FOUND;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DanceServiceImpl implements DanceService {

    private final DanceRepository danceRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final MusicAnswerRepository musicAnswerRepository;

    @Override
    @Transactional
    public void create(DanceCreateRequestDto danceCreateRequestDto) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        User host = userRepository.findById(loginUserId).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceCreateRequestDto.toEntity(host);
        if (!validationDance(randomDance)) {
            throw new DanceBadRequestException(DANCE_INVALID_TIME.getMessage());
        }

        List<Long> danceMusicIdList = danceCreateRequestDto.getDanceMusicIdList();
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
            MusicAnswer musicAnswer = musicAnswerRepository.findById(music.getAnswer())
                    .orElseThrow(() -> new MusicNotFoundException(MUSIC_ANSWER_NOT_FOUND.getMessage()));
            MusicFindResponseDto musicFindResponseDto = MusicFindResponseDto.builder()
                    .music(music).musicAnswer(musicAnswer).build();
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

    @Override
    @Transactional
    public void createAttend(Long randomDanceId) {
        String loginUserId = SecurityUtils.getLoggedInUserId();
        User user = userRepository.findById(loginUserId).orElseThrow(()
                -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        RandomDance randomDance
                = danceRepository.findOne(randomDanceId).orElseThrow(()
                -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

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