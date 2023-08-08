package com.pi.stepup.domain.board.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkResponseDto;
import com.pi.stepup.domain.board.service.talk.TalkService;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pi.stepup.domain.board.constant.TalkApiUrls.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TalkApiController.class)
public class TalkApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @MockBean
    private TalkService talkService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private TalkRequestDto.TalkSaveRequestDto talkSaveRequestDto;
    private TalkRequestDto.TalkUpdateRequestDto talkUpdateRequestDto;
    private TalkResponseDto.TalkInfoResponseDto talkInfoResponseDto;
    private List<TalkResponseDto.TalkInfoResponseDto> allTalks = new ArrayList<>();
    private User writer;
    private Talk talk1;
    private Talk talk2;

    @BeforeEach
    public void init() {
        gson = new Gson();
        makeWriter();
        makeTalk();
        makeTalk2();
        makeTalkSaveRequestDto();
        makeTalkInfoResponseDto();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }


    public void makeTalk() {
        talk1 = Talk.builder()
                .boardId(1L)
                .writer(writer)
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
    }

    public void makeTalk2() {
        talk2 = Talk.builder()
                .boardId(2L)
                .writer(writer)
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
    }

    public void makeTalkSaveRequestDto() {
        talkSaveRequestDto = TalkRequestDto.TalkSaveRequestDto.builder()
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
    }

    public void makeTalkUpdateRequestDto() {
        talkUpdateRequestDto = TalkRequestDto.TalkUpdateRequestDto.builder()
                .boardId(1L)
                .title("(수정)자유게시판 테스트 제목")
                .content("(수정)자유게시판 테스트 내용")
                .fileURL("(수정)https://example.com/talk_files/meeting_document.pdf")
                .build();
    }

    private void makeTalkInfoResponseDto() {
        talkInfoResponseDto = TalkResponseDto.TalkInfoResponseDto.builder()
                .talk(talk1)
                .build();
    }

    @Test
    @DisplayName("자유게시판 게시글 등록 테스트")
    @WithMockUser
    public void createTalkApiTest() throws Exception {
        String url = CREATE_TALK_URL.getUrl();

        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url).with(csrf())
                        .content(gson.toJson(talkSaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
        verify(talkService, times(1)).create(any(TalkRequestDto.TalkSaveRequestDto.class));
    }


    @Test
    @DisplayName("자유게시판 게시글 삭제 테스트")
    @WithMockUser
    public void deleteTalkApiTest() throws Exception {
        Long boardId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
                MockMvcRequestBuilders.delete(DELETE_TALK_URL.getUrl() + boardId)
                        .with(csrf())
        );

        verify(talkService, only()).delete(boardId);
        deleteAction.andExpect(status().isOk());

    }

//    @Test
//    @DisplayName("자유게시판 게시글 수정 테스트")
//    @WithMockUser
//    public void updateTalkApiTest() throws Exception {
//
//        makeTalkUpdateRequestDto();
//        String url = UPDATE_TALK_URL.getUrl();
//
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.put(url)
//                        .with(csrf())
//                        .content(gson.toJson(talkUpdateRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding(StandardCharsets.UTF_8)
//        );
//
//        resultActions.andExpect(status().isOk());
//
//        verify(talkService, times(1)).update(any(TalkRequestDto.TalkUpdateRequestDto.class));
//    }

    @Test
    @DisplayName("자유게시판 게시글 상세 조회 테스트")
    @WithMockUser
    public void readOneTalkControllerTest() throws Exception {
        when(talkService.readOne(any())).thenReturn(Optional.ofNullable(talkInfoResponseDto));

        String url = READ_ONE_TALK_URL.getUrl() + talk1.getBoardId();
        final ResultActions getAction = mockMvc.perform(
                get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }
}
