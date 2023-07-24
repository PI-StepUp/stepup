package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.domain.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository {

    Meeting insert(Meeting meeting);

    Optional<Meeting> findOne(Long boardId);

    List<Meeting> findAll();

    List<Meeting> findAllByKeyword(String keyword);

    void delete(Long boardId);
}
