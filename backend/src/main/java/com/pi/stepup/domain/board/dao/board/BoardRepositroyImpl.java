package com.pi.stepup.domain.board.dao.board;

import com.pi.stepup.domain.board.domain.Board;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Board> findAllBoardByUserId(Long userId) {
        return em.createQuery("SELECT b FROM Board b "
            + "WHERE b.writer.userId = :userId", Board.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public void delete(Board board) {
        em.remove(board);
    }
}
