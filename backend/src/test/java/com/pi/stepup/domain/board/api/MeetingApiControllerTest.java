package com.pi.stepup.domain.board.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto;
import com.pi.stepup.domain.board.service.meeting.MeetingService;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.service.UserRedisService;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.pi.stepup.domain.board.constant.MeetingApiUrls.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingApiController.class)
public class MeetingApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRedisService userRedisService;

    private Gson gson;

    @MockBean
    private MeetingService meetingService;
    private MeetingRequestDto.MeetingSaveRequestDto  meetingSaveRequestDto;
    private MeetingRequestDto.MeetingUpdateRequestDto meetingUpdateRequestDto;
    private MeetingResponseDto.MeetingInfoResponseDto meetingInfoResponseDto;
    private User writer;
    private Meeting meeting1;
    private Meeting meeting2;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");

    @BeforeEach
    public void init() {

        gson = new Gson();
        makeWriter();
        makeMeeting();
        makeMeetingSaveRequestDto();
        makeMeetingInfoResponseDto();
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
                .startAt(LocalDateTime.parse("2023-07-20 10:00", formatter))
                .endAt(LocalDateTime.parse("2023-08-30 10:00", formatter))
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
                .startAt(LocalDateTime.parse("2023-07-20 10:00", formatter))
                .endAt(LocalDateTime.parse("2023-08-30 10:00", formatter))
                .region("광주")
                .build();
    }

    public void makeMeetingSaveRequestDto() {
        meetingSaveRequestDto = MeetingRequestDto.MeetingSaveRequestDto.builder()
                .title("정모 테스트 제목")
                .content("정모 테스트 내용")
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .startAt(LocalDateTime.parse("2023-07-20 10:00", formatter))
                .endAt(LocalDateTime.parse("2023-08-30 10:00", formatter))
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

    private void makeMeetingInfoResponseDto() {
        meetingInfoResponseDto = MeetingResponseDto.MeetingInfoResponseDto.builder()
                .meeting(meeting1)
                .build();
    }

    @Test
    @DisplayName("정모 게시글 등록 테스트")
    @WithMockUser
    public void createMeetingApiTest() throws Exception {
        String url = CREATE_MEETING_URL.getUrl();

        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url).with(csrf())
                        .content(gson.toJson(meetingSaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
        verify(meetingService, times(1)).create(any(MeetingRequestDto.MeetingSaveRequestDto.class));
    }

    @Test
    @DisplayName("정모 게시글 삭제 테스트")
    @WithMockUser
    public void deleteTalkApiTest() throws Exception {
        Long boardId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
                MockMvcRequestBuilders.delete(DELETE_MEETING_URL.getUrl() + boardId)
                        .with(csrf())
        );

        verify(meetingService, only()).delete(boardId);
        deleteAction.andExpect(status().isOk());

    }

    @Test
    @DisplayName("정모 게시글 상세 조회 테스트")
    @WithMockUser
    public void readOneNoticeControllerTest() throws Exception {
        when(meetingService.readOne(any())).thenReturn(meetingInfoResponseDto);

        String url = READ_ONE_MEETING_URL.getUrl() + meeting1.getBoardId();
        final ResultActions getAction = mockMvc.perform(
                get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }
}