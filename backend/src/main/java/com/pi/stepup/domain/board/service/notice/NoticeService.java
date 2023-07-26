package com.pi.stepup.domain.board.service.notice;

import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto.NoticeInfoResponseDto;

import java.util.List;
import java.util.Optional;

public interface NoticeService {
    Notice create(NoticeSaveRequestDto noticeSaveRequestDto);

    Notice update(NoticeUpdateRequestDto noticeUpdateRequestDto);

    List<NoticeInfoResponseDto> readAll(String keyword);

    NoticeInfoResponseDto readOne(Long boardId);

    void delete(Long boardId);

}
