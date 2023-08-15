package com.pi.stepup.domain.board.service.redis;

public interface CntRedisService {
    public void increaseViewCnt(Long boardId);

    public void updateDbAndViewCnt();

}
