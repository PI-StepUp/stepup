package com.pi.stepup.domain.board.dao.redis;

import com.pi.stepup.domain.board.domain.Board;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ViewCntRedisRepository extends CrudRepository<Board, String> {
    @Modifying
    @Query("UPDATE Board b SET b.viewCnt = :viewCnt WHERE b.boardId = :boardId")
    void addViewCntFromRedis(@Param("boardId") Long boardId, @Param("viewCnt") Long viewCnt);
}
