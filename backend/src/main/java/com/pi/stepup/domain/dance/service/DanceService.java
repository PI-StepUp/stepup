package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import java.util.List;

public interface DanceService {

    RandomDance create(DanceCreateRequestDto danceCreateRequestDto);

    RandomDance readOne(Long randomDanceId);

    RandomDance update(DanceUpdateRequestDto danceUpdateRequestDto);

    void delete(Long randomDanceId);

    List<DanceMusic> readAllMusic(Long randomDanceId);
}
