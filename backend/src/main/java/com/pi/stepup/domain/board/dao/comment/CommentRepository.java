package com.pi.stepup.domain.board.dao.comment;

import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Comment;

import java.util.List;

public interface CommentRepository {
    Comment insert (Comment comment);
    List<Comment> findByBoardId(Long boardId);
    void delete(Long commemt);
}
