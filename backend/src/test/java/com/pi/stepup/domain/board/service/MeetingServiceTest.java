package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto;
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
import java.util.Optional;

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

    @Test
    @BeforeEach
    public void init() {
        makeWriter();
        makeMeetingSaveRequestDto1();
        makeMeeting();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }


//    public void makeMeeting() {
//        meeting1 = Meeting.builder()
//                .boardId(1L)
//                .writer(writer)
//                .title("정모 테스트 제목")
//                .content("정모 테스트 내용")
//                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
//                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
//                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
//                .region("서울")
//                .build();
//    }

    private void makeMeeting() {
        meeting1 = Meeting.builder()
                .boardId(1L)
                .title(meetingSaveRequestDto.getTitle())
                .content(meetingSaveRequestDto.getContent())
                .fileURL(meetingSaveRequestDto.getFileURL())
                .startAt(meetingSaveRequestDto.getStartAt())
                .endAt(meetingSaveRequestDto.getEndAt())
                .region(meetingSaveRequestDto.getRegion())
                .writer(writer)
                .build();
    }

    public void makeMeetingSaveRequestDto1() {
        meetingSaveRequestDto = MeetingRequestDto.MeetingSaveRequestDto.builder()
                .title("정모 테스트 제목")
                .content("정모 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-28T09:00:00"))
                .endAt(LocalDateTime.parse("2023-07-28T11:00:00"))
                .region("서울")
                .build();
    }

    public void makeMeetingUpdateRequestDto1() {
        meetingUpdateRequestDto = MeetingRequestDto.MeetingUpdateRequestDto.builder()
                .boardId(1L)
                .title("(수정)정모 테스트 제목")
                .content("(수정)정모 테스트 내용")
                .fileURL("(수정)https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    @Test
    @DisplayName("정모 게시글 등록 테스트")
    public void createMeetingServiceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            when(userRepository.findById(any(String.class))).thenReturn(Optional.of(writer));

            makeMeetingSaveRequestDto1();

            meetingService.create(meetingSaveRequestDto);
            verify(meetingRepository, only()).insert(any(Meeting.class));
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
