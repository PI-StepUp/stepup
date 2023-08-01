package com.pi.stepup.domain.board.service.meeting;

import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingSaveRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingUpdateRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto.MeetingInfoResponseDto;

import java.util.List;

public interface MeetingService {
    Meeting create(MeetingSaveRequestDto meetingSaveRequestDto);

    Meeting update(MeetingUpdateRequestDto meetingUpdateRequestDto);

    List<MeetingInfoResponseDto> readAll(String keyword);

    List<MeetingInfoResponseDto> readAllById();

    MeetingInfoResponseDto readOne(Long boardId);

    void delete(Long boardId);

}
