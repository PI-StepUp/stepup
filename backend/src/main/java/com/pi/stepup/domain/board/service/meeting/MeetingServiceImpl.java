package com.pi.stepup.domain.board.service.meeting;

import com.pi.stepup.domain.board.dao.comment.CommentRepository;
import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingUpdateRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingSaveRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto.MeetingInfoResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Meeting create(MeetingSaveRequestDto meetingSaveRequestDto) {
        User writer = userRepository.findById(meetingSaveRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        Meeting meeting = Meeting.builder()
                .writer(writer)
                .title(meetingSaveRequestDto.getTitle())
                .content(meetingSaveRequestDto.getContent())
                .fileURL(meetingSaveRequestDto.getFileURL())
                .startAt(meetingSaveRequestDto.getStartAt())
                .endAt(meetingSaveRequestDto.getEndAt())
                .region(meetingSaveRequestDto.getRegion())
                .build();

        meetingRepository.insert(meeting);

        return meeting;
    }

    @Transactional
    @Override
    public Meeting update(MeetingUpdateRequestDto meetingUpdateRequestDto) {
        return null;
    }

    @Transactional
    @Override
    public List<MeetingInfoResponseDto> readAll(String keyword) {
        List<Meeting> allMeetings = meetingRepository.findAll(keyword);
        return allMeetings.stream()
                .map(m -> MeetingInfoResponseDto.builder().meeting(m).build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MeetingInfoResponseDto readOne(Long boardId) {
        return MeetingInfoResponseDto.builder()
                .meeting(meetingRepository.findOne(boardId).orElseThrow())
                .build();
    }

    @Transactional
    @Override
    public void delete(Long boardId) {
        meetingRepository.delete(boardId);
    }
}
