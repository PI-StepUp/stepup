package com.pi.stepup.domain.board.service.meeting;

import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingSaveRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingUpdateRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto.MeetingInfoResponseDto;
import com.pi.stepup.domain.board.exception.BoardNotFoundException;
import com.pi.stepup.domain.board.exception.MeetingBadRequestException;
import com.pi.stepup.domain.board.service.comment.CommentService;
import com.pi.stepup.domain.board.service.redis.CntRedisService;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import com.pi.stepup.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.BOARD_NOT_FOUND;
import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.MEETING_INVALID_TIME;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final CntRedisService cntRedisService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional
    @Override
    public Meeting create(MeetingSaveRequestDto meetingSaveRequestDto) {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        User writer = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        LocalDateTime startAt = LocalDateTime.parse(meetingSaveRequestDto.getStartAt(), formatter);
        LocalDateTime endAt = LocalDateTime.parse(meetingSaveRequestDto.getEndAt(), formatter);

        if (endAt.isBefore(startAt)) {
            throw new MeetingBadRequestException(MEETING_INVALID_TIME.getMessage());
        }

        Meeting meeting = Meeting.builder()
                .writer(writer)
                .title(meetingSaveRequestDto.getTitle())
                .content(meetingSaveRequestDto.getContent())
                .fileURL(meetingSaveRequestDto.getFileURL())
                .startAt(startAt)
                .endAt(endAt)
                .region(meetingSaveRequestDto.getRegion())
                .viewCnt(0L)
                .build();

        meetingRepository.insert(meeting);

        return meeting;
    }

    @Transactional
    @Override
    public Meeting update(MeetingUpdateRequestDto meetingUpdateRequestDto) {
        Meeting meeting = meetingRepository.findOne(meetingUpdateRequestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));

        if (LocalDateTime.parse(meetingUpdateRequestDto.getEndAt(), formatter)
                .isBefore(LocalDateTime.parse(meetingUpdateRequestDto.getStartAt(), formatter))) {
            throw new MeetingBadRequestException(MEETING_INVALID_TIME.getMessage());
        }

        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        if (!loggedInUserId.equals(meeting.getWriter().getId())) {
            throw new ForbiddenException();
        }

        meeting.update(meetingUpdateRequestDto.getTitle(), meetingUpdateRequestDto.getContent(),
                meetingUpdateRequestDto.getFileURL(), meetingUpdateRequestDto.getRegion(),
                LocalDateTime.parse(meetingUpdateRequestDto.getStartAt(), formatter),
                LocalDateTime.parse(meetingUpdateRequestDto.getEndAt(), formatter));

        return meeting;
    }

    @Transactional
    @Override
    public List<MeetingInfoResponseDto> readAll(String keyword) {
        List<Meeting> allMeetings = meetingRepository.findAll(keyword);

        List<MeetingInfoResponseDto> meetingInfoResponseDtos = new ArrayList<>();

        for (Meeting meeting : allMeetings) {
            Long currentViewCnt = null;
            if (cntRedisService != null && meeting != null) {
                currentViewCnt = cntRedisService.getViewCntFromRedis(meeting.getBoardId());
            }
            MeetingInfoResponseDto dto = MeetingInfoResponseDto.builder()
                    .meeting(meeting)
                    .viewCnt(currentViewCnt)
                    .build();
            meetingInfoResponseDtos.add(dto);
        }

        return meetingInfoResponseDtos;
    }


    @Override
    public List<MeetingInfoResponseDto> readAllById() {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<Meeting> allMyMeetings = meetingRepository.findById(loggedInUserId);
        return allMyMeetings.stream()
                .map(m -> MeetingInfoResponseDto.builder().meeting(m).build())
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public MeetingInfoResponseDto readOne(Long boardId) {
        Meeting meeting = meetingRepository.findOne(boardId)
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        cntRedisService.increaseViewCnt(boardId);
        Long currentViewCnt = cntRedisService.getViewCntFromRedis(boardId);
        List<CommentInfoResponseDto> comments = commentService.readByBoardId(boardId);
        return MeetingInfoResponseDto.builder()
                .meeting(meetingRepository.findOne(boardId).orElseThrow())
                .comments(comments)
                .viewCnt(currentViewCnt)
                .build();
    }


    @Transactional
    @Override
    public void delete(Long boardId) {
        Meeting meeting = meetingRepository.findOne(boardId).orElseThrow(()
                -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        if (!loggedInUserId.equals(meeting.getWriter().getId()) && !UserRole.ROLE_ADMIN.equals(meeting.getWriter().getRole())) {
            throw new ForbiddenException();
        }
        meetingRepository.delete(boardId);
    }
}
