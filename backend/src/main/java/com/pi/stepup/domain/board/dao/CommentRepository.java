package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.domain.Board;

import java.util.List;

public interface CommentRepository {
    List<Board> findByBoard(Board board);
}
