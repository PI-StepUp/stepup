package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;

public interface PointHistoryService {
    void update(PointUpdateRequestDto pointUpdateRequestDto);
}
