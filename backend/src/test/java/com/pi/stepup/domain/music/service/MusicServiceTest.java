package com.pi.stepup.domain.music.service;

import static org.junit.jupiter.api.Assertions.*;

import com.pi.stepup.domain.music.domain.Music;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MusicServiceTest {

    @Autowired
    private MusicService musicService;

    @Test
    @DisplayName("musicService가 null이 아님을 테스트")
    public void musicServiceNotNullTest(){
        assertNotNull(musicService);
    }

    @Test
    @DisplayName("노래 추가 서비스 테스트")
    @Transactional
    public void insertMusicServiceTest() {
        Music music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();

        Music result = musicService.insert(music);
        assertEquals(result, music);
    }
}