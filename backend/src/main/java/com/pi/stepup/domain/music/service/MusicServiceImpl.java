package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;

    @Override
    @Transactional
    public Music create(MusicSaveRequestDto musicSaveRequestDto) {
        Music music = musicSaveRequestDto.toEntity();
        log.info("노래 생성 : {}", music.getURL());
        return musicRepository.insert(music);
    }

    @Override
    public MusicFindResponseDto readOne(Long musicId) {
        return MusicFindResponseDto.builder()
            .music(musicRepository.findOne(musicId).orElseThrow())
            .build();
    }

    public List<MusicFindResponseDto> readAll(String keyword) {
        return musicRepository.findAll(keyword).stream()
            .map(music -> MusicFindResponseDto.builder()
                .music(music)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long musicId) {
        musicRepository.delete(musicId);
    }
}
