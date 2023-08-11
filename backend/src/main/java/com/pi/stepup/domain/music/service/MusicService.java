package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicUpdateRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;

import java.util.List;

public interface MusicService {
    Music create(MusicSaveRequestDto musicSaveRequestDto);

    void update(MusicUpdateRequestDto musicUpdateRequestDto);

    MusicFindResponseDto readOne(Long musicId);

    List<MusicFindResponseDto> readAll(String keyword);

    void delete(Long musicId);
}
