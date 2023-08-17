package com.pi.stepup.domain.board.dao.notice;

import com.pi.stepup.domain.board.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Notice insert(Notice notice) {
        em.persist(notice);
        return notice;
    }

    @Override
    public Optional<Notice> findOne(Long boardId) {
        try {
            return Optional.ofNullable(em.find(Notice.class, boardId));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Notice> findAll(String keyword) {
        String jpql = "SELECT n FROM Notice n WHERE n.title LIKE :keyword OR n.content LIKE :keyword ORDER BY n.boardId desc";
        return em.createQuery(jpql, Notice.class).setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    @Override
    public void delete(Long boardId) {
        Notice notice = em.find(Notice.class, boardId);
        em.remove(notice);
    }
}
