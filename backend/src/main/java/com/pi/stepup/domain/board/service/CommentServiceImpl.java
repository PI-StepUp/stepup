package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.CommentRepository;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.dto.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.dto.CommentResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;


    @Override
    public CommentSaveRequestDto insert(CommentSaveRequestDto comment) {
        return null;
    }

    @Override
    public Comment update(Comment comment) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId) {

    }

    @Override
    public List<CommentResponseDto> findAllComments(Long boardId) {
        return null;
    }
}
