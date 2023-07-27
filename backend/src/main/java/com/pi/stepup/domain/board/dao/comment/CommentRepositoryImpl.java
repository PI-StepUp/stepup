package com.pi.stepup.domain.board.dao.comment;

import com.pi.stepup.domain.board.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment insert(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public List<Comment> findByBoardId(Long boardId) {
        try {
            String jpql = "SELECT c FROM Comment c WHERE c.board.id = :boardId";
            return em.createQuery(jpql, Comment.class)
                    .setParameter("boardId", boardId)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("댓글 찾기 실패", e);
        }
    }

    @Override
    public void delete(Long commemtId) {
        Comment comment = em.find(Comment.class, commemtId);
        if (comment != null) {
            em.remove(comment);
        } else {
            throw new IllegalArgumentException("댓글 없음.");
        }
    }
}
