package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.dto.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    // 댓글 등록
    public CommentSaveRequestDto create(CommentSaveRequestDto comment);

    // 댓글 수정
    public Comment update(Comment comment);

    // 댓글 삭제
    public void delete(Long commentId);

    // 댓글 조회
    public List<CommentResponseDto> readAll(Long boardId);


}
