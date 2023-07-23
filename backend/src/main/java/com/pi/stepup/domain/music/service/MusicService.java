package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;

import java.util.List;
import java.util.Optional;

public interface MusicService {
    Music create(MusicSaveRequestDto musicSaveRequestDto);

    Optional<Music> readOne(Long musicId);

    List<MusicFindResponseDto> readAll(String keyword);

    void delete(Long musicId);
}
