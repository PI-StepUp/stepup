package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto;
import com.pi.stepup.domain.board.dto.talk.TalkResponseDto;
import com.pi.stepup.domain.board.service.meeting.MeetingServiceImpl;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.config.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {
    @InjectMocks
    MeetingServiceImpl meetingService;
    @Mock
    MeetingRepository meetingRepository;
    @Mock
    UserRepository userRepository;
    private MeetingRequestDto.MeetingSaveRequestDto meetingSaveRequestDto;
    private MeetingRequestDto.MeetingUpdateRequestDto meetingUpdateRequestDto;
    private User writer;
    private Meeting meeting1;
    private Meeting meeting2;

    @Test
    @BeforeEach
    public void init() {
        makeWriter();
        makeMeeting();
        makeMeeting2();
        makeMeetingSaveRequestDto();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }


    public void makeMeeting() {
        meeting1 = Meeting.builder()
                .boardId(1L)
                .writer(writer)
                .title("정모 테스트 제목")
                .content("정모 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("서울")
                .build();
    }

    public void makeMeeting2() {
        meeting2 = Meeting.builder()
                .boardId(2L)
                .writer(writer)
                .title("정모 테스트 제목2")
                .content("정모 테스트 내용2")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("광주")
                .build();
    }

    public void makeMeetingSaveRequestDto() {
        meetingSaveRequestDto = MeetingRequestDto.MeetingSaveRequestDto.builder()
                .title("정모 테스트 제목")
                .content("정모 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("서울")
                .build();
    }

    public void makeMeetingUpdateRequestDto() {
        meetingUpdateRequestDto = MeetingRequestDto.MeetingUpdateRequestDto.builder()
                .boardId(1L)
                .title("(수정)정모 테스트 제목")
                .content("(수정)정모 테스트 내용")
                .fileURL("(수정)https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("광주")
                .build();
    }

    @Test
    @DisplayName("정모 게시글 등록 테스트")
    public void createTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            when(userRepository.findById(any(String.class))).thenReturn(Optional.of(writer));

            makeMeetingSaveRequestDto();

            meetingService.create(meetingSaveRequestDto);
            verify(meetingRepository, only()).insert(any(Meeting.class));
        }
    }

    @Test
    @DisplayName("정모 게시글 수정 테스트")
    public void updateTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            when(meetingRepository.findOne(any(Long.class)))
                    .thenReturn(Optional.of(meeting1));
            makeMeetingUpdateRequestDto();

            assertThatNoException().isThrownBy(() -> meetingService.update(meetingUpdateRequestDto));
        }
    }

    @Test
    @DisplayName("정모 게시글 목록 조회 테스트")
    public void readAllTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            String keyword = "제목";

            List<Meeting> makeMeetings = new ArrayList<>();
            makeMeetings.add(meeting1);
            makeMeetings.add(meeting2);

            when(meetingRepository.findAll(anyString())).thenReturn(makeMeetings);

            List<MeetingResponseDto.MeetingInfoResponseDto> result = meetingService.readAll(keyword);

            // 테스트 결과 검증
            assert(result.size() == 2);
            assert(result.get(0).getTitle().equals("정모 테스트 제목"));
            assert(result.get(1).getTitle().equals("정모 테스트 제목2"));
        }
    }

    @Test
    @DisplayName("내가 쓴 정모 게시글 조회 테스트")
    public void readOneTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());

            List<Meeting> myMeetings = new ArrayList<>();
            myMeetings.add(meeting1);
            myMeetings.add(meeting2);

            when(meetingRepository.findById("j3beom")).thenReturn(myMeetings);

            List<MeetingResponseDto.MeetingInfoResponseDto> result = meetingService.readAllById();
            // Then
            assert(result.size() == 2);
            assertEquals("정모 테스트 제목", result.get(0).getTitle());
            assertEquals("정모 테스트 내용", result.get(0).getContent());
        }
    }

    @Test
    @DisplayName("정모 게시글 삭제 테스트")
    public void deleteTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            // Given
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId).thenReturn(writer.getId());
            when(meetingRepository.findOne(any(Long.class))).thenReturn(Optional.of(meeting1));
            // When
            meetingService.delete(1L);
            // Then
            verify(meetingRepository, times(1)).delete(1L);
        }
    }
}
