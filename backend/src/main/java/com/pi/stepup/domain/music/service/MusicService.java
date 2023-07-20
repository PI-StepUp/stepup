package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;

public interface MusicService {
    Music create(MusicSaveRequestDto musicSaveRequestDto);
    Music readOne(Long musicId);
}
