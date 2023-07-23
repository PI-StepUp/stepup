package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.domain.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository  {

    Notice insert(Notice notice);

    Optional<Notice> findById(Long boardId);

    List<Notice> findAll();

    List<Notice> findAllByKeyword(String keyword);

    void delete(Long boardId);

}
