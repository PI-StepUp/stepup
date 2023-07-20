package com.pi.stepup.domain.music.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MusicServiceTest {

    @Autowired
    private MusicService musicService;
    private MusicSaveRequestDto music;

    @Test
    @BeforeEach
    public void init(){
        // private final 이어서 테스트 코드 작성할 때 이렇게 해야 하는 것 같은데 어쩔 수 없는 건지??
        // 아니면 Builder 이용가능하게?
        music = new MusicSaveRequestDto(
            "spicy",
            "aespa",
            "",
            "url"
        );
    }

    @Test
    @DisplayName("musicService가 null이 아님을 테스트")
    public void musicServiceNotNullTest() {
        assertThat(musicService).isNotNull();
    }

    @Test
    @DisplayName("노래 추가 서비스 테스트")
    @Transactional
    public void createMusicServiceTest() {
        Music result = musicService.create(music);
        assertThat(result.getTitle()).isEqualTo(music.getTitle());
    }


}