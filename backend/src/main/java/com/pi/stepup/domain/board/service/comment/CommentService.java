package com.pi.stepup.domain.board.service.comment;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto;

import java.util.List;

public interface CommentService {
    Comment create(CommentSaveRequestDto commentSaveRequestDto);

    List<CommentInfoResponseDto> readByBoardId(Long boardId);

    void delete(Long commentId);
}
