package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository {
    List<Board> findByBoard(Board board);
}
