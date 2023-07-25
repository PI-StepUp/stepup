package com.pi.stepup.domain.board.dao.meeting;

import com.pi.stepup.domain.board.domain.Meeting;

import java.util.List;
import java.util.Optional;

public class MeetingRepositoryImpl implements MeetingRepository {
    @Override
    public Meeting insert(Meeting meeting) {
        return null;
    }

    @Override
    public Optional<Meeting> findOne(Long boardId) {
        return Optional.empty();
    }

    @Override
    public List<Meeting> findAll() {
        return null;
    }

    @Override
    public List<Meeting> findAllByKeyword(String keyword) {
        return null;
    }

    @Override
    public void delete(Long boardId) {

    }
}
