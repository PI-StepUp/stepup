package com.pi.stepup.domain.board.dao.meeting;

import com.pi.stepup.domain.board.domain.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository {

    Meeting insert(Meeting meeting);

    Optional<Meeting> findOne(Long boardId);

    List<Meeting> findById(String id);

    List<Meeting> findAll(String keyword);

    void delete(Long boardId);
}
