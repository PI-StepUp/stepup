package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicRequest;
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
class MusicRequestRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MusicRequestRepository musicRequestRepository;

    private MusicRequest musicRequest;
    private User user;

    @Test
    @BeforeEach
    public void init() {
        user = User.builder()
                .id("user")
                .build();

        musicRequest = MusicRequest.builder()
                .title("title")
                .artist("artist")
                .content("content")
                .user(user)
                .build();
    }

    @Test
    @DisplayName("repository가 null이 아님을 테스트")
    public void musicRequestRepositoryNotNullTest() {
        assertThat(musicRequestRepository).isNotNull();
    }

    @Test
    @DisplayName("노래 신청 등록 테스트")
    public void insertMusicRequestRepositoryTest() {
        MusicRequest result = musicRequestRepository.insert(musicRequest);
        assertThat(result).isEqualTo(musicRequest);
    }
}