package com.pi.stepup.domain.board.dao.talk;

import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Talk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Talk> findById(String id) {
        try {
            String jpql = "SELECT m FROM Talk m WHERE m.writer.id = :id";
            return em.createQuery(jpql, Talk.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("자유게시핀 검색 오류", e);
        }
    }

    @Override
    public List<Talk> findAll(String keyword) {
        try {
            String jpql = "SELECT t FROM Talk t WHERE t.title LIKE :keyword OR t.content LIKE :keyword";
            return em.createQuery(jpql, Talk.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("자유게시핀 검색 오류", e);
        }
    }

    @Override
    public void delete(Long boardId) {
        Talk talk = em.find(Talk.class, boardId);
        if (talk != null) {
            em.remove(talk);
        } else {
            throw new IllegalArgumentException("게시글 없음.");
        }
    }
}
