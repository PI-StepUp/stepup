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
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        User writer = userRepository.findById(commentSaveRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음."));

        Board board = boardRepository.findOne(commentSaveRequestDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없음."));

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
        List<Comment> allComments = commentRepository.findByBoardId(boardId);
        return allComments.stream()
                .map(c -> CommentInfoResponseDto.builder().comment(c).build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(Long commentId) {
        commentRepository.delete(commentId);
    }
}
