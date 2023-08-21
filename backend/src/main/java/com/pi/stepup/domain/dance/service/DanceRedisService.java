package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import java.util.List;

public interface DanceRedisService {

    void createReservation(Long randomDanceId);

    void deleteReservation(Long randomDanceId);

    List<DanceSearchResponseDto> readAllRandomDance(DanceSearchRequestDto danceSearchRequestDto);

    List<DanceFindResponseDto> readAllMyReserveDance();

}