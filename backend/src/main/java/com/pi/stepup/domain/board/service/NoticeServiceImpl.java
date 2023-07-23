package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.NoticeRepository;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.dto.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private NoticeRepository noticeRepository;

    //게시글 저장
    @Transactional
    @Override
    public Long saveNotice(NoticeSaveRequestDto noticeSaveRequestDto) {
        return noticeRepository.insert(noticeSaveRequestDto.toEntity()).getBoardId();
    }

    //게시글 수정
    @Transactional
    @Override
    public Long updateNotice(Long boardId, NoticeUpdateRequestDto noticeUpdateRequestDto){
        Notice notice = noticeRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("게시글 없음"));

        notice.updateNotice(noticeUpdateRequestDto.getTitle(), noticeUpdateRequestDto.getContent(),
                noticeUpdateRequestDto.getFileURL(), noticeUpdateRequestDto.getRandomDance());

        return boardId;
    }

    //게시글 전체 조회
    @Transactional
    public List<NoticeResponseDto> findAllNotice() {
        List<Notice> allNotices = noticeRepository.findAll();
        return allNotices.stream()
                .map(NoticeResponseDto::new)
                .collect(Collectors.toList());
    }

    //게시글 상세
    @Transactional
    @Override
    public NoticeResponseDto findOneNotice(Long boardId) {
        Optional<Notice> noticeWrapper = noticeRepository.findById(boardId);
        Notice notice = noticeWrapper.get();

        NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        return noticeResponseDto;

    }

    //게시글 삭제
    @Transactional
    @Override
    public void deleteNotie(Long boardId) {
        noticeRepository.delete(boardId);
    }

    //게시글 검색
    @Transactional
    @Override
    public List<NoticeSaveRequestDto> searchNotice(String keyword) {

        List<Notice> noticeEntities = noticeRepository.findAllByKeyword(keyword);
        List<NoticeSaveRequestDto> noticeList = new ArrayList<>();

        if (noticeEntities.isEmpty()) return noticeList;

        for (Notice notice : noticeEntities) {
            NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
                    .writer(notice.getWriter())
                    .randomDance(notice.getRandomDance())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .fileURL(notice.getFileURL())
                    .build();
            noticeList.add(noticeSaveRequestDto);
        }

        return noticeList;
    }
}
