package com.pi.stepup.domain.board.dao.notice;

import com.pi.stepup.domain.board.domain.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {

    Notice insert(Notice notice);

    Optional<Notice> findOne(Long boardId);

    List<Notice> findAll(String keyword);

    void delete(Long boardId);

}
