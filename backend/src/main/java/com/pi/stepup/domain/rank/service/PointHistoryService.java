package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.dto.RankResponseDto.PointHistoryFindResponseDto;

import java.util.List;

public interface PointHistoryService {
    void update(PointUpdateRequestDto pointUpdateRequestDto);

    List<PointHistoryFindResponseDto> readAll();

    Integer readPoint();
}
