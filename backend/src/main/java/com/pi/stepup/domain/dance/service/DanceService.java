package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dto.DanceRequestDto.*;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;

import java.util.List;

public interface DanceService {

    void create(DanceCreateRequestDto danceCreateRequestDto);

    void update(DanceUpdateRequestDto danceUpdateRequestDto);

    void delete(Long randomDanceId);

    List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId);

    List<DanceFindResponseDto> readAllMyOpenDance();

    List<DanceSearchResponseDto> readAllRandomDance(DanceSearchRequestDto danceSearchRequestDto);

    void createReservation(Long randomDanceId);

    void deleteReservation(Long reservationId);

    List<DanceFindResponseDto> readAllMyReserveDance();

    void createAttend(Long randomDanceId);

    List<DanceFindResponseDto> readAllMyAttendDance();


}
