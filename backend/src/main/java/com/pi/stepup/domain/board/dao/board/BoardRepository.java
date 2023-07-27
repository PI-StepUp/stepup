package com.pi.stepup.domain.board.dao.board;

import com.pi.stepup.domain.board.domain.Board;

import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findOne(Long boardId);

}
