package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MusicServiceImpl implements MusicService{

    private final MusicRepository musicRepository;

    @Override
    public Music create(MusicSaveRequestDto musicSaveRequestDto) {
        Music music = Music.builder()
            .title(musicSaveRequestDto.getTitle())
            .artist(musicSaveRequestDto.getArtist())
            .answer(musicSaveRequestDto.getAnswer())
            .URL(musicSaveRequestDto.getURL())
            .build();

        return musicRepository.insert(music);
    }
}
