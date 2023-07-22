package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;

import java.util.Optional;

public interface MusicService {
    Music create(MusicSaveRequestDto musicSaveRequestDto);
    Optional<Music> readOne(Long musicId);
}
