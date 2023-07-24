package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;

public interface MusicApplyService {
    MusicApply create(MusicApplySaveRequestDto musicApplySaveRequestDto);
}
