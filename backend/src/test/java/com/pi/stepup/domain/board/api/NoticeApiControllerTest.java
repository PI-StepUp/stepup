package com.pi.stepup.domain.board.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto;
import com.pi.stepup.domain.board.service.notice.NoticeService;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.domain.User;
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
import java.util.ArrayList;
import java.util.List;

import static com.pi.stepup.domain.board.constant.NoticeApiUrls.*;
import static com.pi.stepup.domain.board.constant.TalkApiUrls.READ_ONE_TALK_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeApiController.class)
public class NoticeApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private Gson gson;

    @MockBean
    private NoticeService noticeService;
    private NoticeRequestDto.NoticeSaveRequestDto noticeSaveRequestDto;
    private NoticeRequestDto.NoticeUpdateRequestDto noticeUpdateRequestDto;
    private NoticeResponseDto.NoticeInfoResponseDto noticeInfoResponseDto;
    private List<NoticeResponseDto.NoticeInfoResponseDto> allTalks = new ArrayList<>();
    private User writer;
    private User adminUser;
    private Notice notice1;
    private Notice notice2;
    private RandomDance randomDance;
    private Music music;
    private DanceMusic danceMusic;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");

    @BeforeEach
    public void init() {
        gson = new Gson();
        makeWriter();
        makeAdmin();
        makeMusic();
        makeDance();
        makeDanceMusic();
        makeNotice();
        makeNotice2();
        makeNoticeSaveRequestDto();
        makeNoticeInfoResponseDto();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }

    public User makeAdmin() {
        adminUser = User.builder()
                .id("adminUserId")
                .role(UserRole.ROLE_ADMIN)
                .build();
        return adminUser;
    }

    public void makeNotice() {
        randomDance = makeDance();
        notice1 = Notice.builder()
                .boardId(1L)
                .writer(adminUser)
                .title("공지 테스트 제목")
                .content("공지 테스트 내용")
                .randomDance(randomDance)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public void makeNotice2() {
        randomDance = makeDance();
        notice2 = Notice.builder()
                .boardId(2L)
                .writer(writer)
                .title("공지 테스트 제목2")
                .content("공지 테스트 내용2")
                .randomDance(randomDance)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public RandomDance makeDance() {
        randomDance = RandomDance.builder()
                .randomDanceId(1L)
                .title("랜덤 플레이 댄스")
                .content("함께 합시다")
                .startAt(LocalDateTime.parse("2023-07-20 10:00", formatter))
                .endAt(LocalDateTime.parse("2023-08-30 10:00", formatter))
                .build();
        return randomDance;
    }

    public void makeNoticeSaveRequestDto() {
        noticeSaveRequestDto = NoticeRequestDto.NoticeSaveRequestDto.builder()
                .title("공지 테스트 제목")
                .content("공지 테스트 내용")
                .randomDanceId(1L)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public void makeNoticeUpdateRequestDto() {
        noticeUpdateRequestDto = NoticeRequestDto.NoticeUpdateRequestDto.builder()
                .boardId(1L)
                .title("(수정)공지 테스트 제목")
                .content("(수정)공지 테스트 내용")
                .randomDanceId(1L)
                .fileURL("(수정)https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public Music makeMusic() {
        music = Music.builder()
                .musicId(1L)
                .title("mTitle")
                .artist("artist")
                .build();
        return music;
    }

    public void makeDanceMusic() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
    }

    private void makeNoticeInfoResponseDto() {
        noticeInfoResponseDto = NoticeResponseDto.NoticeInfoResponseDto.builder()
                .notice(notice1)
                .build();
    }

    @Test
    @DisplayName("공지 게시글 등록 테스트")
    @WithMockUser
    public void createNoticeApiTest() throws Exception {
        String url = CREATE_NOTICE_URL.getUrl();

        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url).with(csrf())
                        .content(gson.toJson(noticeSaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
        verify(noticeService, times(1)).create(any(NoticeRequestDto.NoticeSaveRequestDto.class));
    }


    @Test
    @DisplayName("공지 게시글 삭제 테스트")
    @WithMockUser
    public void deleteTalkApiTest() throws Exception {
        Long boardId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
                MockMvcRequestBuilders.delete(DELETE_NOTICE_URL.getUrl() + boardId)
                        .with(csrf())
        );

        verify(noticeService, only()).delete(boardId);
        deleteAction.andExpect(status().isOk());

    }

    @Test
    @DisplayName("공지 게시글 상세 조회 테스트")
    @WithMockUser
    public void readOneNoticeControllerTest() throws Exception {
        when(noticeService.readOne(any())).thenReturn(noticeInfoResponseDto);

        String url = READ_ONE_NOTICE_URL.getUrl() + notice1.getBoardId();
        final ResultActions getAction = mockMvc.perform(
                get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }
}
