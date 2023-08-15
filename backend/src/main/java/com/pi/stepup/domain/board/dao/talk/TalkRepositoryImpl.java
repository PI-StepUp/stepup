package com.pi.stepup.domain.board.dao.talk;

import com.pi.stepup.domain.board.domain.Talk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TalkRepositoryImpl implements TalkRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Talk insert(Talk talk) {
        em.persist(talk);
        return talk;
    }

    @Override
    public Optional<Talk> findOne(Long boardId) {
        try {
            return Optional.ofNullable(em.find(Talk.class, boardId));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Talk> findById(String id) {
        String jpql = "SELECT t FROM Talk t WHERE t.writer.id = :id ORDER BY t.boardId desc";
        return em.createQuery(jpql, Talk.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Talk> findAll(String keyword) {
        String jpql = "SELECT t FROM Talk t WHERE t.title LIKE :keyword OR t.content LIKE :keyword ORDER BY t.boardId desc";
        return em.createQuery(jpql, Talk.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    @Override
    public void delete(Long boardId) {
        Talk talk = em.find(Talk.class, boardId);
        em.remove(talk);
    }
}
