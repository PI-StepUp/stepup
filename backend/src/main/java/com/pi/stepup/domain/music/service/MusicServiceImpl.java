package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicFindByKeywordRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MusicServiceImpl implements MusicService {

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

    @Override
    public Optional<Music> readOne(Long musicId) {
        return musicRepository.findOne(musicId);
    }

    public List<MusicFindResponseDto> readAll() {
        return musicRepository.findAll().stream()
                .map(music -> MusicFindResponseDto.builder()
                        .music(music)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicFindResponseDto> readAllByKeyword(MusicFindByKeywordRequestDto musicFindByKeywordRequestDto) {
        return musicRepository.findAllByKeyword(musicFindByKeywordRequestDto.getKeyword()).stream()
                .map(music -> MusicFindResponseDto.builder()
                        .music(music)
                        .build())
                .collect(Collectors.toList());
    }


}
