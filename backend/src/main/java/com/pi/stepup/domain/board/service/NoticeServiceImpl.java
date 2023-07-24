package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.NoticeRepository;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.NoticeResponseDto;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final DanceRepository danceRepository;
    private final UserRepository userRepository;


    //게시글 저장
//    @Transactional
//    @Override
//    public Long create(NoticeSaveRequestDto noticeSaveRequestDto) {
//        return noticeRepositoryImpl.insert(noticeSaveRequestDto.toEntity()).getBoardId();
//    }

    @Transactional
    @Override
    public void create(NoticeSaveRequestDto noticeSaveRequestDto) {
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

        noticeRepository.insert(notice);
    }

    //게시글 수정
//    @Transactional
//    @Override
//    public Long update(Long boardId, NoticeUpdateRequestDto noticeUpdateRequestDto){
//        Notice notice = noticeRepository.findOne(boardId).orElseThrow(()-> new IllegalArgumentException("게시글 없음"));
//
//        notice.update(noticeUpdateRequestDto.getTitle(), noticeUpdateRequestDto.getContent(),
//                noticeUpdateRequestDto.getFileURL(), noticeUpdateRequestDto.getRandomDance());
//
//        return boardId;
//    }
//
//    //게시글 전체 조회
//    @Transactional
//    public List<NoticeResponseDto> readAll() {
//        List<Notice> allNotices = noticeRepository.findAll();
//        return allNotices.stream()
//                .map(NoticeResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
    //게시글 상세
    @Transactional
    @Override
    public NoticeResponseDto readOne(Long boardId) {
        Optional<Notice> noticeWrapper = noticeRepository.findOne(boardId);
        //Notice notice = noticeWrapper.get();
        Notice notice = noticeWrapper.orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지않음"));

        NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        return noticeResponseDto;

    }
//
//    //게시글 삭제
//    @Transactional
//    @Override
//    public void delete(Long boardId) {
//        noticeRepository.delete(boardId);
//    }

    //게시글 검색
//    @Transactional
//    @Override
//    public List<NoticeSaveRequestDto> searchNotice(String keyword) {
//
//        List<Notice> noticeEntities = noticeRepository.findAllByKeyword(keyword);
//        List<NoticeSaveRequestDto> noticeList = new ArrayList<>();
//
//        if (noticeEntities.isEmpty()) return noticeList;
//
//        for (Notice notice : noticeEntities) {
//            NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
//                    .writer(notice.getWriter())
//                    .randomDance(notice.getRandomDance())
//                    .title(notice.getTitle())
//                    .content(notice.getContent())
//                    .fileURL(notice.getFileURL())
//                    .build();
//            noticeList.add(noticeSaveRequestDto);
//        }
//
//        return noticeList;
//    }
}
