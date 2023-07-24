package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.NoticeResponseDto;

public interface NoticeService {
    public void create(NoticeSaveRequestDto noticeSaveRequestDto);

    // public Long update(Long boardId, NoticeUpdateRequestDto noticeUpdateRequestDto);

    // public List<NoticeResponseDto> readAll();

    public NoticeResponseDto readOne(Long boardId);

    // public void delete(Long boardId);

    // public List<NoticeSaveRequestDto> searchNotice(String keyword);

}
