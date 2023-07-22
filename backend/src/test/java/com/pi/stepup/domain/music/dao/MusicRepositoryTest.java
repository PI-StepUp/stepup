package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MusicRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MusicRepository musicRepository;
    private Music music;

    @Test
    @BeforeEach
    public void init() {
        music = Music.builder()
                .title("spicy")
                .artist("aespa")
                .answer("")
                .URL("url")
                .build();
    }

    @Test
    @DisplayName("MusicRepository가 null이 아님을 테스트")
    public void musicRepositoryNotNullTest() {
        assertThat(musicRepository).isNotNull();
    }

    @Test
    @DisplayName("노래 추가 테스트")
    public void insertMusicRepoTest() {
        Music result = musicRepository.insert(music);
        assertThat(music).isEqualTo(result);
    }

    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    public void findOneMusicRepoTest() {
        em.persist(music);

        Long musicId = music.getMusicId();
        Optional<Music> result = musicRepository.findOne(musicId);
        Music resultMusic = null;
        if (result.isPresent()) {
            resultMusic = result.get();
        }
        assertThat(music.getTitle()).isEqualTo(resultMusic.getTitle());
    }

    @Test
    @DisplayName("노래 전체 목록 조회 테스트")
    public void findAllMusicRepoTest() {
        insertMusic();

        List<Music> music = musicRepository.findAll();
        assertThat(music.size()).isEqualTo(5);
    }

    private List<Music> makeMusic() {
        List<Music> music = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Music tmp = Music.builder().title("title" + i).artist("artist" + (i + 1)).build();
            music.add(tmp);
        }
        return music;
    }

    private void insertMusic() {
        List<Music> music = makeMusic();
        music.forEach(em::persist);
    }

    @Test
    @DisplayName("노래 전체 목록 키워드 조회 테스트")
    public void findAllMusicByKeywordRepoTest() {
        insertMusic();

        String keyword = "1";
        List<Music> music = musicRepository.findAllByKeyword(keyword);
        assertThat(music.size()).isEqualTo(2);
    }
}