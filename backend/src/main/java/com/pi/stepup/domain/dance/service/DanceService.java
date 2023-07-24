package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSaveRequestDto;

public interface DanceService {

    RandomDance create(DanceSaveRequestDto danceSaveRequestDto);

    RandomDance readOne(Long randomDanceId);

    RandomDance update(DanceSaveRequestDto danceSaveRequestDto);

    void delete(Long randomDanceId);
}
