package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DanceServiceImpl implements DanceService {

    private final DanceRepository danceRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");

    @Override
    @Transactional
    public RandomDance createDance(DanceCreateRequestDto danceCreateRequestDto) {
        RandomDance randomDance
                = RandomDance.builder()
                .title(danceCreateRequestDto.getTitle())
                .content(danceCreateRequestDto.getContent())
                .startAt(LocalDateTime.parse(danceCreateRequestDto.getStartAt(), formatter))
                .endAt(LocalDateTime.parse(danceCreateRequestDto.getEndAt(), formatter))
                .danceType(danceCreateRequestDto.getDanceType())
                .maxUser(danceCreateRequestDto.getMaxUser())
                .thumbnail(danceCreateRequestDto.getThumbnail())
                .host(userRepository.findById(danceCreateRequestDto.getHostId()).orElseThrow())
                .build();

        List<Long> danceMusicIdList = danceCreateRequestDto.getDanceMusicIdList();
        for (int i = 0; i < danceMusicIdList.size(); i++) {
            Music music = musicRepository.findOne(danceMusicIdList.get(i)).orElseThrow();
            DanceMusic.createDanceMusic(music, randomDance);
        }

        RandomDance createdDance = danceRepository.insert(randomDance);
        return createdDance;
    }

    @Override
    public RandomDance readDance(Long randomDanceId) {
        return danceRepository.findOne(randomDanceId).orElseThrow();
    }

    @Override
    @Transactional
    public RandomDance updateDance(DanceUpdateRequestDto danceUpdateRequestDto) {
//        RandomDance randomDance = danceUpdateRequestDto.toEntity();
//        return danceRepository.update(randomDance);
        return null;
    }

    @Override
    @Transactional
    public void deleteDance(Long randomDanceId) {
        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<Music> readAllDanceMusic(Long randomDanceId) {
        List<Music> allMusic = new ArrayList<>();

        List<DanceMusic> danceMusicList = danceRepository.findAllDanceMusic(randomDanceId);
        for (int i = 0; i < danceMusicList.size(); i++) {
            Long musicId = danceMusicList.get(i).getMusic().getMusicId();
            Music findMusic = musicRepository.findOne(musicId).orElseThrow();
            allMusic.add(findMusic);
        }
        return allMusic;
    }

    @Override
    public List<RandomDance> readAllMyHeldDance(String id) {
        return danceRepository.findAllMyHeldDance(id);
    }

}