package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.dao.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RandomDance create(DanceCreateRequestDto danceCreateRequestDto) {
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
//            DanceMusic danceMusic
//                = DanceMusic.builder()
////                .randomDance(randomDance)
//                .music(music)
//                .build();
            DanceMusic.createDanceMusic(music, randomDance);

//            randomDance.addDanceMusicAndSetThis(danceMusic);
//            danceMusic.setRandomDanceAndAddThis(randomDance);
        }

        RandomDance createdDance = danceRepository.insert(randomDance);
        return createdDance;
    }

    @Override
    public RandomDance readOne(Long randomDanceId) {
        return danceRepository.findOne(randomDanceId).orElseThrow();
    }

    @Override
    @Transactional
    public RandomDance update(DanceUpdateRequestDto danceUpdateRequestDto) {
//        RandomDance randomDance = danceUpdateRequestDto.toEntity();
//        return danceRepository.update(randomDance);
        return null;
    }

    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<DanceMusic> readAllMusic(Long randomDanceId) {
//        RandomDance findDance = danceRepository.findOne(randomDanceId);
//        danceRepository.findAllMusic(randomDanceId);
//        return findDance.getDanceMusicList();
        return null;
    }

}