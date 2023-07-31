package com.pi.stepup.domain.board.service.talk;

import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkSaveRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkUpdateRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkResponseDto.TalkInfoResponseDto;

import java.util.List;
import java.util.Optional;

public interface TalkService {
    Talk create(TalkSaveRequestDto talkSaveRequestDto);

    Talk update(TalkUpdateRequestDto talkUpdateRequestDto);

    List<TalkInfoResponseDto> readAll(String keyword);

    List<TalkInfoResponseDto> readAllById();

    Optional<TalkInfoResponseDto> readOne(Long boardId);

    void delete(Long boardId);

}
