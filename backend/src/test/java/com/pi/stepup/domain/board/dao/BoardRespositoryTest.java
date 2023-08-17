package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.dao.board.BoardRepository;
import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class BoardRespositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BoardRepository boardRepository;

    private User writer;
    private Talk talk;
    private Meeting meeting;

    @Test
    @BeforeEach
    public void init() {
        writer = User.builder()
                .id("j3beom")
                .build();

        em.persist(writer);
        em.flush();

        talk = Talk.builder()
                .writer(writer)
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();

        em.persist(talk);
        em.flush();

        meeting = Meeting.builder()
                .writer(writer)
                .title("정모 테스트 제목")
                .content("정모 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("서울, 대한민국")
                .build();

        em.persist(meeting);
        em.flush();
    }

    @Test
    @DisplayName("게시글 상세 조회 테스트")
    public void testFindOne() {
        Optional<Board> foundBoard = boardRepository.findOne(meeting.getBoardId());
        assertTrue(foundBoard.isPresent());
        assertThat(foundBoard.get().getBoardId()).isEqualTo(meeting.getBoardId());
    }
}
