package com.pi.stepup.domain.board.service.meeting;

import com.pi.stepup.domain.board.dao.meeting.MeetingRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingSaveRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingUpdateRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto.MeetingInfoResponseDto;
import com.pi.stepup.domain.board.exception.BoardNotFoundException;
import com.pi.stepup.domain.board.service.comment.CommentService;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.BOARD_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Transactional
    @Override
    public Meeting create(MeetingSaveRequestDto meetingSaveRequestDto) {
        User writer = userRepository.findById(meetingSaveRequestDto.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

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
        Meeting meeting = meetingRepository.findOne(meetingUpdateRequestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));

        // 로그인한 사용자의 정보 가져오기
        String loggedInUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 로그인한 사용자가 작성자가 아닌 경우 ForbiddenException 발생
        if (!loggedInUserId.equals(meeting.getWriter().getId())) {
            throw new ForbiddenException();
        }

        meeting.update(meetingUpdateRequestDto.getTitle(), meetingUpdateRequestDto.getContent(),
                meetingUpdateRequestDto.getFileURL(), meetingUpdateRequestDto.getRegion(),
                meetingUpdateRequestDto.getStartAt(), meetingUpdateRequestDto.getEndAt());

        return meeting;
    }

    @Transactional
    @Override
    public List<MeetingInfoResponseDto> readAll(String keyword) {
        List<Meeting> allMeetings = meetingRepository.findAll(keyword);
        return allMeetings.stream()
                .map(m -> MeetingInfoResponseDto.builder().meeting(m).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingInfoResponseDto> readAllById(String id) {
        List<Meeting> allMyMeetings = meetingRepository.findById(id);
        return allMyMeetings.stream()
                .map(m -> MeetingInfoResponseDto.builder().meeting(m).build())
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public MeetingInfoResponseDto readOne(Long boardId) {
        meetingRepository.findOne(boardId).orElseThrow(()
                -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        List<CommentInfoResponseDto> comments = commentService.readByBoardId(boardId);
        return MeetingInfoResponseDto.builder()
                .meeting(meetingRepository.findOne(boardId).orElseThrow())
                .comments(comments)
                .build();
    }

    @Transactional
    @Override
    public void delete(Long boardId) {
        Meeting meeting = meetingRepository.findOne(boardId).orElseThrow(()
                -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));

        String loggedInUserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        // 로그인한 사용자가 댓글 작성자이거나, 관리자일 경우에만 삭제 허용
        if (!loggedInUserId.equals(meeting.getWriter().getId()) && !UserRole.ROLE_ADMIN.equals(meeting.getWriter().getRole())) {
            throw new ForbiddenException();
        }
        meetingRepository.delete(boardId);
    }
}
