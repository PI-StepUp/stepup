package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.NoticeRepository;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto.NoticeInfoResponseDto;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final DanceRepository danceRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);


    @Transactional
    @Override
    public Notice create(NoticeSaveRequestDto noticeSaveRequestDto) {
        User writer = userRepository.findById(noticeSaveRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID: " + noticeSaveRequestDto.getId()));

        RandomDance randomDance = danceRepository.findOne(noticeSaveRequestDto.getRandomDanceId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 랜덤댄 ID: " + noticeSaveRequestDto.getRandomDanceId()));

        Notice notice = Notice.builder()
                .writer(writer)
                .randomDance(randomDance)
                .title(noticeSaveRequestDto.getTitle())
                .content(noticeSaveRequestDto.getContent())
                .fileURL(noticeSaveRequestDto.getFileURL())
                .build();

        Notice insertedNotice = noticeRepository.insert(notice);

        logger.debug("[create()] insertedNotice : {}", insertedNotice);

        return notice;
    }

    // 게시글 수정
    @Transactional
    @Override
    public Notice update(NoticeUpdateRequestDto noticeUpdateRequestDto) {

        Notice notice = noticeRepository.findOne(noticeUpdateRequestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        RandomDance randomDance = danceRepository.findOne(noticeUpdateRequestDto.getRandomDanceId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 랜덤댄 ID: " + noticeUpdateRequestDto.getRandomDanceId()));

        notice.update(noticeUpdateRequestDto.getTitle(), noticeUpdateRequestDto.getContent(),
                noticeUpdateRequestDto.getFileURL(), randomDance);
        return notice;
    }


    //게시글 전체 조회
    @Transactional
    @Override
    public List<NoticeInfoResponseDto> readAll(String keyword) {
        List<Notice> allNotices = noticeRepository.findAll(keyword);
        return allNotices.stream()
                .map(c -> NoticeInfoResponseDto.builder().notice(c).build())
                .collect(Collectors.toList());
    }


    //게시글 상세
    @Transactional
    @Override
    public Optional<NoticeInfoResponseDto> readOne(Long boardId) {

        return Optional.ofNullable(NoticeInfoResponseDto.builder()
                .notice(noticeRepository.findOne(boardId).orElseThrow())
                .build());
    }

    //게시글 삭제
    @Transactional
    @Override
    public void delete(Long boardId) {
        noticeRepository.delete(boardId);
    }

}
