package com.pi.stepup.domain.board.dao.meeting;

import com.pi.stepup.domain.board.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Meeting> findById(String id) {
        String jpql = "SELECT m FROM Meeting m WHERE m.writer.id = :id ORDER BY m.boardId desc";
        return em.createQuery(jpql, Meeting.class)
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public List<Meeting> findAll(String keyword) {
            String jpql = "SELECT m FROM Meeting m WHERE m.title LIKE :keyword OR m.content Like :keyword";
            return em.createQuery(jpql, Meeting.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
    }


    @Override
    public void delete(Long boardId) {
        Meeting meeting = em.find(Meeting.class, boardId);
            em.remove(meeting);
    }
}
