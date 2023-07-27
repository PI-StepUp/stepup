package com.pi.stepup.domain.board.dao.meeting;

import com.pi.stepup.domain.board.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meeting insert(Meeting meeting) {
        em.persist(meeting);
        return meeting;
    }

    @Override
    public Optional<Meeting> findOne(Long boardId) {
        try {
            return Optional.ofNullable(em.find(Meeting.class, boardId));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    @Override
    public List<Meeting> findById(String id) {
        try {
            String jpql = "SELECT m FROM Meeting m WHERE m.writer.id = :id";
        return em.createQuery(jpql, Meeting.class)
                .setParameter("id", id)
                .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("정모 검색 오류", e);
        }
    }

    @Override
    public List<Meeting> findAll(String keyword) {
        try {
            String jpql = "SELECT m FROM Meeting m WHERE m.title LIKE :keyword OR m.content Like :keyword";
            return em.createQuery(jpql, Meeting.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("정모 검색 오류", e);
        }
    }


    @Override
    public void delete(Long boardId) {
        Meeting meeting = em.find(Meeting.class, boardId);
        if (meeting != null) {
            em.remove(meeting);
        } else {
            throw new IllegalArgumentException("게시글 없음.");
        }
    }
}
