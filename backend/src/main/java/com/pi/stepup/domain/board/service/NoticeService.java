package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.NoticeRequestDto;
import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.dto.NoticeResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;

public interface NoticeService {
    public Long saveNotice(NoticeSaveRequestDto noticeSaveRequestDto);

    public Long updateNotice(Long boardId, NoticeUpdateRequestDto noticeUpdateRequestDto);

    public List<NoticeResponseDto> findAllNotice();

    public NoticeResponseDto findOneNotice(Long boardId);

    public void deleteNotie(Long boardId);

    public List<NoticeSaveRequestDto> searchNotice(String keyword);

}
