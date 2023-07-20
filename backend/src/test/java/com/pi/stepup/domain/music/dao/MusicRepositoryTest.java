package com.pi.stepup.domain.music.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pi.stepup.domain.music.domain.Music;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MusicRepositoryTest {

    @Autowired
    private MusicRepository musicRepository;

    @Test
    @DisplayName("MusicRepository가 null이 아님을 테스트")
    public void musicRepositoryNotNullTest(){
        assertNotNull(musicRepository);
    }

    @Test
    @DisplayName("노래 추가 테스트")
    @Transactional
    public void insertMusicTest(){
        Music music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();
        Music result = musicRepository.insert(music);
        assertEquals(music, result);
    }

    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    public void selectOneMusicTest(){
        Music music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();
        musicRepository.insert(music);

        Long musicId = 1L;
        Music result = musicRepository.selectOne(musicId);
        assertEquals(musicId, result.getMusicId());
    }

}