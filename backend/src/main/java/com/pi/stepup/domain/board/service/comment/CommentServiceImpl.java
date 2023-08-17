package com.pi.stepup.domain.board.service.comment;

import com.pi.stepup.domain.board.dao.board.BoardRepository;
import com.pi.stepup.domain.board.dao.comment.CommentRepository;
import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.dao.talk.TalkRepository;
import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import com.pi.stepup.domain.board.exception.BoardNotFoundException;
import com.pi.stepup.domain.board.exception.CommentNotFoundException;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import com.pi.stepup.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.BOARD_NOT_FOUND;
import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.COMMENT_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final MeetingRepository meetingRepository;
    private final TalkRepository talkRepository;

    @Transactional
    @Override
    public Comment create(CommentSaveRequestDto commentSaveRequestDto) {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        User writer = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        Board board = boardRepository.findOne(commentSaveRequestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));

        Comment comment = Comment.builder()
                .writer(writer)
                .board(board)
                .content(commentSaveRequestDto.getContent())
                .build();

        Comment savedComment = commentRepository.insert(comment);

        if (board instanceof Talk) {
            Talk talk = (Talk) board;
            talk.updateCommentCnt(); // 댓글 수 업데이트
            talkRepository.insert(talk);
        } else if (board instanceof Meeting) {
            Meeting meeting = (Meeting) board;
            meeting.updateCommentCnt(); // 댓글 수 업데이트
            meetingRepository.insert(meeting);
        }

        return savedComment;
    }


    @Transactional
    @Override
    public List<CommentInfoResponseDto> readByBoardId(Long boardId) {
        boardRepository.findOne(boardId)
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        List<Comment> allComments = commentRepository.findByBoardId(boardId);
        return allComments.stream()
                .map(c -> CommentInfoResponseDto.builder().comment(c).build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(Long commentId) {
        Comment comment = commentRepository.findOne(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND.getMessage()));

        String loggedInUserId = SecurityUtils.getLoggedInUserId();

        if (!loggedInUserId.equals(comment.getWriter().getId()) && !UserRole.ROLE_ADMIN.equals(comment.getWriter().getRole())) {
            throw new ForbiddenException();
        }

        commentRepository.delete(commentId);
    }
}
