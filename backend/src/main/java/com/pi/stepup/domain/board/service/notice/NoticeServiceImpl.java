package com.pi.stepup.domain.board.service.notice;

import com.pi.stepup.domain.board.dao.notice.NoticeRepository;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto.NoticeInfoResponseDto;
import com.pi.stepup.domain.board.exception.BoardNotFoundException;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import com.pi.stepup.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.BOARD_NOT_FOUND;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final DanceRepository danceRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Notice create(NoticeSaveRequestDto noticeSaveRequestDto) {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        User writer = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        if (!writer.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }

        RandomDance randomDance = danceRepository.findOne(noticeSaveRequestDto.getRandomDanceId())
                .orElseThrow(() -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        Notice notice = Notice.builder()
                .writer(writer)
                .randomDance(randomDance)
                .title(noticeSaveRequestDto.getTitle())
                .content(noticeSaveRequestDto.getContent())
                .fileURL(noticeSaveRequestDto.getFileURL())
                .viewCnt(0L)
                .build();

        noticeRepository.insert(notice);

        return notice;
    }

    @Transactional
    @Override
    public Notice update(NoticeUpdateRequestDto noticeUpdateRequestDto) {

        Notice notice = noticeRepository.findOne(noticeUpdateRequestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));

        RandomDance randomDance = danceRepository.findOne(noticeUpdateRequestDto.getRandomDanceId())
                .orElseThrow(() -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));

        String loggedInUserId = SecurityUtils.getLoggedInUserId();

        if (!loggedInUserId.equals(notice.getWriter().getId())) {
            throw new ForbiddenException();
        }

        notice.update(noticeUpdateRequestDto.getTitle(), noticeUpdateRequestDto.getContent(),
                noticeUpdateRequestDto.getFileURL(), randomDance);
        return notice;
    }

    @Transactional
    @Override
    public List<NoticeInfoResponseDto> readAll(String keyword) {
        List<Notice> allNotices = noticeRepository.findAll(keyword);
        return allNotices.stream()
                .map(n -> NoticeInfoResponseDto.builder().notice(n).build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public NoticeInfoResponseDto readOne(Long boardId) {
        Notice notice = noticeRepository.findOne(boardId)
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        notice.increaseViewCnt();
        return NoticeInfoResponseDto.builder()
                .notice(noticeRepository.findOne(boardId).orElseThrow())
                .build();
    }

    @Transactional
    @Override
    public void delete(Long boardId) {
        Notice notice = noticeRepository.findOne(boardId).orElseThrow(()
                -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));

        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        if (!loggedInUserId.equals(notice.getWriter().getId()) && !UserRole.ROLE_ADMIN.equals(notice.getWriter().getRole())) {
            throw new ForbiddenException();
        }

        noticeRepository.delete(boardId);
    }

}
