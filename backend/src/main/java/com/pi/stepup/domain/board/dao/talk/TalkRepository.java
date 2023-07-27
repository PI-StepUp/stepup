package com.pi.stepup.domain.board.dao.talk;

import com.pi.stepup.domain.board.domain.Talk;

import java.util.List;
import java.util.Optional;

public interface TalkRepository {
    Talk insert(Talk talk);

    Optional<Talk> findOne(Long boardId);

    List<Talk> findById(String id);

    List<Talk> findAll(String keyword);

    void delete(Long boardId);
}
