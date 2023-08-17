package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.board.BoardRepository;
import com.pi.stepup.domain.board.dao.comment.CommentRepository;
import com.pi.stepup.domain.board.dao.talk.TalkRepository;
import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto;
import com.pi.stepup.domain.board.service.comment.CommentServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentServiceImpl commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    TalkRepository talkRepository;
    @Mock
    UserRepository userRepository;
    private CommentSaveRequestDto commentSaveRequestDto;
    private User writer;
    private Board board;
    private Talk talk;
    private Comment comment1;
    private Comment comment2;

    @Test
    @BeforeEach
    public void init() {
        makeWriter();
        makeBoard();
        makeTalk();
        makeComment();
        makeComment2();
        makeCommentSaveRequestDto();
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
        commentSaveRequestDto = CommentSaveRequestDto.builder()
                .boardId(1L)
                .content("댓글입니당")
                .build();
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    public void createTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            when(userRepository.findById(any(String.class))).thenReturn(Optional.of(writer));
            when(boardRepository.findOne(any(long.class))).thenReturn(Optional.of((Board) talk));
            makeCommentSaveRequestDto();
            commentService.create(commentSaveRequestDto);
            verify(commentRepository, only()).insert(any(Comment.class));
        }
    }

    @Test
    @DisplayName("댓글 하나 조회 테스트")
    public void readOneTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());

            List<Comment> comments = new ArrayList<>();
            comments.add(comment1);
            comments.add(comment2);

            when(boardRepository.findOne(1L)).thenReturn(Optional.of(board));
            when(commentRepository.findByBoardId(1L)).thenReturn(comments);

            List<CommentResponseDto.CommentInfoResponseDto> result = commentService.readByBoardId(1L);

            assert(result.size() == 2);
            assert(result.get(0).getContent().equals("댓글입니당"));
            assert(result.get(1).getContent().equals("댓글입니다아아아"));
        }
    }


    @Test
    @DisplayName("자유게시판 게시글 삭제 테스트")
    public void deleteTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {

            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId).thenReturn(writer.getId());
            when(commentRepository.findOne(any(Long.class))).thenReturn(Optional.of(comment1));

            commentService.delete(1L);

            verify(commentRepository, times(1)).delete(1L);
        }
    }
}
