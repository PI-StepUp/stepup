package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class MeetingRespositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MeetingRepository meetingRepository;

    private User writer;
    private Meeting meeting1;
    private Meeting meeting2;

    @Test
    @BeforeEach
    public void init() {
        writer = User.builder()
                .id("j3beom")
                .build();

        em.persist(writer);
        em.flush();

        meeting1 = Meeting.builder()
                .writer(writer)
                .title("정모 테스트 제목")
                .content("정모 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("서울, 대한민국")
                .build();


        meeting2 = Meeting.builder()
                .writer(writer)
                .title("정모 테스트 제목2")
                .content("정모 테스트 내용2")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-30T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("광주, 대한민국")
                .build();
    }

    @Test
    @DisplayName("정모 게시글 등록 테스트")
    public void testInsert() {
        // When
        Meeting savedMeeting1 = meetingRepository.insert(meeting1);
        Meeting savedMeeting2 = meetingRepository.insert(meeting2);
        // Then
        assertThat(savedMeeting1.getBoardId()).isNotNull();
        assertThat(savedMeeting2.getBoardId()).isNotNull();
    }

    @Test
    @DisplayName("정모 게시글 상세 조회 테스트")
    public void testFindOne() {
        // Given
        testInsert();
        // When
        Optional<Meeting> foundMeeting = meetingRepository.findOne(meeting1.getBoardId());

        // Then
        assertTrue(foundMeeting.isPresent());
        assertThat(foundMeeting.get().getBoardId()).isEqualTo(meeting1.getBoardId());
    }

    @Test
    @DisplayName("내가 쓴 정모 게시글 조회 테스트")
    public void testFindById() {
        // Given
        testInsert();

        // When
        List<Meeting> meetings = meetingRepository.findById("j3beom");
        int numberOfMeetingsFound = meetings.size();

        // Then
        assertThat(2).isEqualTo(numberOfMeetingsFound);
    }

    @Test
    @DisplayName("정모 게시글 전체 조회 테스트")
    public void testFindAll() {
        // Given
        testInsert();

        // When
        List<Meeting> meetings = meetingRepository.findAll("");
        int numberOfMeetingsFound = meetings.size();

        // Then
        assertThat(2).isEqualTo(numberOfMeetingsFound);
    }

    @Test
    @DisplayName("정모 게시글 삭제 테스트")
    public void testDelete() {
        // Given
        testInsert();

        // When
        meetingRepository.delete(meeting1.getBoardId());

        // Then
        Meeting deletedMeeting = em.find(Meeting.class, meeting1.getBoardId());
        assertNull(deletedMeeting);
    }
}

