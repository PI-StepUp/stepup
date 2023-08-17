package com.pi.stepup.domain.board.dao.board;

import com.pi.stepup.domain.board.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findOne(Long boardId);

    List<Board> findAllBoardByUserId(Long userId);

    void delete(Board board);

}
