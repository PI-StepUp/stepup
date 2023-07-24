package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.user.domain.User;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MusicApplyRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MusicApplyRepository musicApplyRepository;

    private MusicApply musicApply;
    private User writer;

    @Test
    @BeforeEach
    public void init() {
        writer = User.builder()
                .id("user")
                .build();

        musicApply = MusicApply.builder()
                .title("title")
                .artist("artist")
                .content("content")
                .writer(writer)
                .build();
    }

    @Test
    @DisplayName("repository가 null이 아님을 테스트")
    public void musicApplyRepositoryNotNullTest() {
        assertThat(musicApplyRepository).isNotNull();
    }

    @Test
    @DisplayName("노래 신청 등록 테스트")
    public void insertMusicApplyRepositoryTest() {
        MusicApply result = musicApplyRepository.insert(musicApply);
        assertThat(result).isEqualTo(musicApply);
    }

    @Test
    @DisplayName("노래 신청 목록 조회 테스트")
    public void findAllMusicApplyRepositoryTest() {
        String keyword = "";
        insertMusicApply();

        List<MusicApply> musicApplies = musicApplyRepository.findAll(keyword);
        assertThat(musicApplies.size()).isEqualTo(5);
    }

    private List<MusicApply> makeMusicApply() {
        List<MusicApply> musicApplies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MusicApply musicApply = MusicApply.builder()
                    .title("t" + i)
                    .artist("a" + (i + 1))
                    .build();

            musicApplies.add(musicApply);
        }
        return musicApplies;
    }

    private void insertMusicApply() {
        List<MusicApply> musicApplies = makeMusicApply();
        musicApplies.forEach(em::persist);
    }
}