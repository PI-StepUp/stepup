package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.dao.talk.TalkRepository;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class TalkRespositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TalkRepository talkRepository;

    private User writer;
    private Talk talk1;
    private Talk talk2;

    @Test
    @BeforeEach
    public void init() {
        writer = User.builder()
                .id("j3beom")
                .build();

        em.persist(writer);
        em.flush();

        talk1 = Talk.builder()
                .writer(writer)
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();

        talk2 = Talk.builder()
                .writer(writer)
                .title("자유게시판 테스트 제목2")
                .content("자유게시판 테스트 내용2")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    @Test
    @DisplayName("자유게시판 게시글 등록 테스트")
    public void testInsert() {
        Talk savedTalk1 = talkRepository.insert(talk1);
        Talk savedTalk2 = talkRepository.insert(talk2);

        assertThat(savedTalk1.getBoardId()).isNotNull();
        assertThat(savedTalk2.getBoardId()).isNotNull();
    }

    @Test
    @DisplayName("자유게시판 게시글 상세 조회 테스트")
    public void testFindOne() {
        testInsert();

        Optional<Talk> foundMeeting = talkRepository.findOne(talk1.getBoardId());

        assertTrue(foundMeeting.isPresent());
        assertThat(foundMeeting.get().getBoardId()).isEqualTo(talk1.getBoardId());
    }

    @Test
    @DisplayName("내가 쓴 자유게시판 게시글 조회 테스트")
    public void testFindById() {
        testInsert();

        List<Talk> talks = talkRepository.findById("j3beom");
        int numberOfMeetingsFound = talks.size();

        assertThat(2).isEqualTo(numberOfMeetingsFound);
    }

    @Test
    @DisplayName("자유게시판 게시글 전체 조회 테스트")
    public void testFindAll() {
        testInsert();

        List<Talk> talks = talkRepository.findAll("");
        int numberOfMeetingsFound = talks.size();

        assertThat(2).isEqualTo(numberOfMeetingsFound);
    }

    @Test
    @DisplayName("자유게시판 게시글 삭제 테스트")
    public void testDelete() {
        testInsert();

        talkRepository.delete(talk1.getBoardId());

        Talk deletedMeeting = em.find(Talk.class, talk1.getBoardId());
        assertNull(deletedMeeting);
    }
}
