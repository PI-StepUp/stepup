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
    public void musicRequestRepositoryNotNullTest() {
        assertThat(musicApplyRepository).isNotNull();
    }

    @Test
    @DisplayName("노래 신청 등록 테스트")
    public void insertMusicRequestRepositoryTest() {
        MusicApply result = musicApplyRepository.insert(musicApply);
        assertThat(result).isEqualTo(musicApply);
    }
}