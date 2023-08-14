package com.pi.stepup.domain.board.api;


import com.google.gson.Gson;
import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto;
import com.pi.stepup.domain.board.service.comment.CommentService;
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

import static com.pi.stepup.domain.board.constant.CommentApiUrls.CREATE_COMMENT_URL;
import static com.pi.stepup.domain.board.constant.CommentApiUrls.DELETE_COMMENT_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentApiController.class)
public class CommentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRedisService userRedisService;

    private Gson gson;

    @MockBean
    private CommentService commentService;
    private User writer;
    private Board board;
    private Talk talk;
    private Comment comment1;
    private Comment comment2;
    private CommentResponseDto.CommentInfoResponseDto commentInfoResponseDto;
    private CommentRequestDto.CommentSaveRequestDto commentSaveRequestDto;

    @BeforeEach
    public void init() {

        gson = new Gson();
        makeWriter();
        makeBoard();
        makeTalk();
        makeComment();
        makeComment2();
        makeCommentSaveRequestDto();
        makeCommentInfoResponseDto();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }

    public Board makeBoard() {
        board = Talk.builder()
                .boardId(1L)
                .writer(writer)
                .boardType("TALK")
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .commentCnt(0)
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
        return board;
    }

    public Talk makeTalk() {
        talk = Talk.builder()
                .boardId(1L)
                .writer(writer)
                .boardType("TALK")
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .commentCnt(0)
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
        return talk;
    }

    public void makeComment() {
        comment1 = Comment.builder()
                .board(board)
                .writer(writer)
                .content("댓글입니당")
                .build();
    }

    public void makeComment2() {
        comment2 = Comment.builder()
                .board(board)
                .writer(writer)
                .content("댓글입니다아아아")
                .build();
    }

    public void makeCommentSaveRequestDto() {
        commentSaveRequestDto = CommentRequestDto.CommentSaveRequestDto.builder()
                .boardId(1L)
                .content("댓글입니당")
                .build();
    }

    private void makeCommentInfoResponseDto() {
        commentInfoResponseDto = CommentResponseDto.CommentInfoResponseDto.builder()
                .comment(comment1)
                .build();
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    @WithMockUser
    public void createCommentApiTest() throws Exception {
        Long boardId = 1L;
        String url = CREATE_COMMENT_URL.getUrl() + boardId;

        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url).with(csrf())
                        .content(gson.toJson(commentSaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
        verify(commentService, times(1)).create(any(CommentRequestDto.CommentSaveRequestDto.class));
    }

    @Test
    @DisplayName("댓글 게시글 삭제 테스트")
    @WithMockUser
    public void deleteCommentApiTest() throws Exception {
        Long commentId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
                MockMvcRequestBuilders.delete(DELETE_COMMENT_URL.getUrl() + commentId)
                        .with(csrf())
        );

        verify(commentService, only()).delete(commentId);
        deleteAction.andExpect(status().isOk());

    }

}
