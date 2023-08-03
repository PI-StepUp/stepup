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
        makeOneMusic();
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
        String keyword = "";
        List<Music> musics = musicRepository.findAll(keyword);
        assertThat(musics.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("노래 전체 목록 키워드 조회 테스트")
    public void findAllMusicByKeywordRepoTest() {
        insertMusic();

        String keyword = "1";
        List<Music> music = musicRepository.findAll(keyword);
        assertThat(music.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("노래 삭제 테스트")
    public void deleteMusicRepoTest() {
        insertMusic();

        Long musicId = 1L;
        musicRepository.delete(musicId);

        // 아래 방법 사용 안하고 테스트 어떻게..??
        Optional<Music> result = musicRepository.findOne(musicId);
        assertThat(result).isNotPresent();
    }

    private List<Music> makeMusic() {
        List<Music> musics = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Music tmp = Music.builder().title("title" + i).artist("artist" + (i + 1)).build();
            musics.add(tmp);
        }
        return musics;
    }

    private List<Music> insertMusic() {
        List<Music> musics = makeMusic();
        musics.forEach(em::persist);
        return musics;
    }

    private void makeOneMusic() {
        music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();
    }
}