package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.music.domain.Music;

import java.util.List;

public interface DanceService {

    RandomDance createDance(DanceCreateRequestDto danceCreateRequestDto);

    RandomDance readDance(Long randomDanceId);

    RandomDance updateDance(DanceUpdateRequestDto danceUpdateRequestDto);

    void deleteDance(Long randomDanceId);

    List<Music> readAllDanceMusic(Long randomDanceId);

    List<RandomDance> readAllMyHeldDance(String id);
}
