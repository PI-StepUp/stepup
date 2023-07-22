package com.pi.stepup.domain.music.service;


import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MusicServiceTest {
    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private MusicRepository musicRepository;

    private MusicSaveRequestDto musicSaveRequestDto;
    private Music music;

    @Test
    @BeforeEach
    public void init() {
        musicSaveRequestDto = MusicSaveRequestDto.builder()
                .title("spicy")
                .artist("aespa")
                .answer("")
                .URL("url")
                .build();
        music = musicSaveRequestDto.toEntity();
    }

    @Test
    @DisplayName("노래 추가 서비스 테스트")
    @Transactional
    public void createMusicServiceTest() {
        when(musicRepository.insert(any(Music.class))).thenReturn(music);

        Music result = musicService.create(musicSaveRequestDto);
        assertThat(result.getTitle()).isEqualTo(music.getTitle());
    }

    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    public void readOneMusicServiceTest() {
        when(musicRepository.findOne(any())).thenReturn(Optional.of(music));

        Optional<Music> result = musicService.readOne(music.getMusicId());
        Music foundMusic = null;
        if(result.isPresent()) {
            foundMusic = result.get();
        }
        assertThat(music).isEqualTo(foundMusic);
    }
}