package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.CommentRepository;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;


    @Override
    public CommentSaveRequestDto create(CommentSaveRequestDto comment) {
        return null;
    }

    @Override
    public Comment update(Comment comment) {
        return null;
    }

    @Override
    public void delete(Long commentId) {

    }

    @Override
    public List<CommentResponseDto> readAll(Long boardId) {
        return null;
    }
}
