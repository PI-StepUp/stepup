package com.pi.stepup.domain.board.dao.board;

import com.pi.stepup.domain.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositroyImpl implements BoardRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Board> findOne(Long boardId) {
        try {
            return Optional.ofNullable(em.find(Board.class, boardId));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
