package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;

import java.util.List;

public interface MusicApplyService {
    MusicApply create(MusicApplySaveRequestDto musicApplySaveRequestDto);

    List<MusicApplyFindResponseDto> readAll(String keyword);

    MusicApplyFindResponseDto readOne(Long musicApplyId);
}
