package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Heart;
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
import java.util.Optional;

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
    private Heart heart;

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

        heart = Heart.builder()
                .user(writer)
                .musicApply(musicApply)
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

    @Test
    @DisplayName("노래 신청 목록 키워드 조회 테스트")
    public void findAllMusicApplyByKeywordRepositoryTest() {
        String keyword = "1";
        insertMusicApply();

        List<MusicApply> musicApplies = musicApplyRepository.findAll(keyword);
        assertThat(musicApplies.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("노래 신청 목록 사용자 아이디로 조회 테스트")
    public void findAllMusicApplyByUserRepositoryTest() {
        insertWriter();
        insertMusicApplyWithDifferentWriter();

        List<MusicApply> musicApplies = musicApplyRepository.findById(writer.getId());
        assertThat(musicApplies.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("노래 신청 상세 조회 테스트")
    public void findOneMusicApplyRepositoryTest() {
        em.persist(musicApply);

        Long musicApplyId = musicApply.getMusicApplyId();
        Optional<MusicApply> result = musicApplyRepository.findOne(musicApplyId);
        MusicApply resultMusicApply = null;
        if (result.isPresent()) {
            resultMusicApply = result.get();
        }

        assertThat(resultMusicApply).isEqualTo(musicApply);
    }

    @Test
    @DisplayName("노래 신청 삭제 테스트")
    public void deleteMusicApplyRepositoryTest() {
        em.persist(musicApply);

        Long musicApplyId = musicApply.getMusicApplyId();
        musicApplyRepository.delete(musicApplyId);

        assertThat(musicApplyRepository.findOne(musicApplyId)).isEmpty();
    }

    private List<MusicApply> makeMusicApply() {
        List<MusicApply> musicApplies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MusicApply musicApply = MusicApply.builder()
                    .title("t" + i)
                    .artist("a" + (i + 1))
                    .writer(writer)
                    .build();

            musicApplies.add(musicApply);
        }
        return musicApplies;
    }

    private void insertMusicApplyWithDifferentWriter() {
        List<MusicApply> musicApplies = makeMusicApply();

        User tmp = User.builder().id("another").password("password").build();
        em.persist(tmp);

        musicApplies.add(MusicApply.builder()
                .title("title")
                .writer(tmp)
                .build());

        musicApplies.forEach(em::persist);
    }

    private void insertMusicApply() {
        List<MusicApply> musicApplies = makeMusicApply();
        musicApplies.forEach(em::persist);
    }

    private void insertWriter() {
        em.persist(writer);
    }

    @Test
    @DisplayName("노래 신청 좋아요 테스트")
    public void musicApplyHeartRepositoryTest() {
        em.persist(heart);

        Heart result = musicApplyRepository.insert(heart);

        assertThat(result).isEqualTo(heart);
    }
}