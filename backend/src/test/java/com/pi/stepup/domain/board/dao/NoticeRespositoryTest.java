package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.dao.notice.NoticeRepository;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.dance.domain.RandomDance;
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
public class NoticeRespositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private NoticeRepository noticeRepository;

    private User writer;
    private RandomDance randomDance;
    private Notice notice1;
    private Notice notice2;

    @Test
    @BeforeEach
    public void init() {
        writer = User.builder()
                .id("j3beom")
                .build();

        em.persist(writer);

        randomDance = RandomDance.builder()
                .build();

        em.persist(randomDance);

        notice1 = Notice.builder()
                .writer(writer)
                .title("공지 테스트 제목")
                .content("공지 테스트 내용")
                .randomDance(randomDance)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();

        notice2 = Notice.builder()
                .writer(writer)
                .title("공지 테스트 제목2")
                .content("공지 테스트 내용2")
                .randomDance(randomDance)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }


    @Test
    @DisplayName("공지 게시글 등록 테스트")
    public void testInsert() {
        // When
        Notice savedNotice1 = noticeRepository.insert(notice1);
        Notice savedNotice2 = noticeRepository.insert(notice2);
        // Then
        assertThat(savedNotice1.getBoardId()).isNotNull();
        assertThat(savedNotice2.getBoardId()).isNotNull();
    }

    @Test
    @DisplayName("공지 게시글 상세 조회 테스트")
    public void testFindOne() {
        testInsert();

        Optional<Notice> foundNotice = noticeRepository.findOne(notice1.getBoardId());

        assertTrue(foundNotice.isPresent());
        assertThat(foundNotice.get().getBoardId()).isEqualTo(notice1.getBoardId());
    }

    @Test
    @DisplayName("공지 게시글 전체 조회 테스트")
    public void testFindAll() {
        testInsert();

        List<Notice> notices = noticeRepository.findAll("");
        int numberOfMeetingsFound = notices.size();

        assertThat(2).isEqualTo(numberOfMeetingsFound);
    }

    @Test
    @DisplayName("공지 게시글 삭제 테스트")
    public void testDelete() {
        testInsert();

        noticeRepository.delete(notice1.getBoardId());

        Notice deletedNotice = em.find(Notice.class, notice1.getBoardId());
        assertNull(deletedNotice);
    }
}
