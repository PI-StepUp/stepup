package com.pi.stepup.domain.music.service;


import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicFindByKeywordRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
        if (result.isPresent()) {
            foundMusic = result.get();
        }
        assertThat(music).isEqualTo(foundMusic);
    }

    @Test
    @DisplayName("노래 전체 목록 조회 테스트")
    public void readAllMusicServiceTest() {
        List<Music> makedMusic = makeMusic();
        doReturn(makedMusic)
                .when(musicRepository)
                .findAll();

        List<MusicFindResponseDto> foundMusic = musicService.readAll();

        assertThat(makedMusic.size()).isEqualTo(foundMusic.size());
    }

    private List<Music> makeMusic() {
        List<Music> music = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Music tmp = Music.builder().title("title" + i).artist("artist" + (i + 1)).build();
            music.add(tmp);
        }
        return music;
    }

    @Test
    @DisplayName("노래 키워드 조회 테스트")
    public void readAllByKeywordServiceTest() {
        List<Music> makedMusic = makeMusic();
        String keyword = "1";
        doReturn(makedMusic)
                .when(musicRepository)
                .findAllByKeyword(keyword);

        MusicFindByKeywordRequestDto requestDto = MusicFindByKeywordRequestDto.builder()
                .keyword(keyword)
                .build();
        List<MusicFindResponseDto> foundMusic = musicService.readAllByKeyword(requestDto);

        assertThat(makedMusic.size()).isEqualTo(foundMusic.size());
    }

}