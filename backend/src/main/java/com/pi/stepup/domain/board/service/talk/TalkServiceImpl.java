package com.pi.stepup.domain.board.service.talk;

import com.pi.stepup.domain.board.dao.talk.TalkRepository;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkSaveRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkUpdateRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkResponseDto.TalkInfoResponseDto;
import com.pi.stepup.domain.board.exception.BoardNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pi.stepup.domain.board.constant.BoardExceptionMessage.BOARD_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TalkServiceImpl implements TalkService {

    private final TalkRepository talkRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final CntRedisService cntRedisService;

    @Transactional
    @Override
    public Talk create(TalkSaveRequestDto talkSaveRequestDto) {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        User writer = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        Talk talk = Talk.builder()
                .writer(writer)
                .title(talkSaveRequestDto.getTitle())
                .content(talkSaveRequestDto.getContent())
                .fileURL(talkSaveRequestDto.getFileURL())
                .viewCnt(0L)
                .build();
        talkRepository.insert(talk);
        return talk;
    }

    @Transactional
    @Override
    public Talk update(TalkUpdateRequestDto talkUpdateRequestDto) {
        Talk talk = talkRepository.findOne(talkUpdateRequestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        if (!loggedInUserId.equals(talk.getWriter().getId())) {
            throw new ForbiddenException();
        }
        talk.update(talkUpdateRequestDto.getTitle(), talkUpdateRequestDto.getContent(), talkUpdateRequestDto.getFileURL());
        return talk;
    }


    @Transactional
    @Override
    public List<TalkInfoResponseDto> readAll(String keyword) {
        List<Talk> allTalks = talkRepository.findAll(keyword);

        List<TalkInfoResponseDto> talkInfoResponseDtos = new ArrayList<>();

        for (Talk talk : allTalks) {
            Long currentViewCnt = null;
            if (cntRedisService != null && talk != null) {
                currentViewCnt = cntRedisService.getViewCntFromRedis(talk.getBoardId());
            }
            TalkInfoResponseDto dto = TalkInfoResponseDto.builder()
                    .talk(talk)
                    .viewCnt(currentViewCnt)
                    .build();
            talkInfoResponseDtos.add(dto);
        }

        return talkInfoResponseDtos;
    }

    @Override
    public List<TalkInfoResponseDto> readAllById() {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<Talk> allMyTalks = talkRepository.findById(loggedInUserId);
        return allMyTalks.stream()
                .map(m -> TalkInfoResponseDto.builder().talk(m).build())
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public Optional<TalkInfoResponseDto> readOne(Long boardId) {
        Talk talk = talkRepository.findOne(boardId)
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        cntRedisService.increaseViewCnt(boardId);
        Long currentViewCnt = cntRedisService.getViewCntFromRedis(boardId);
        List<CommentInfoResponseDto> comments = commentService.readByBoardId(boardId);
        return Optional.ofNullable(TalkInfoResponseDto.builder()
                .talk(talkRepository.findOne(boardId).orElseThrow())
                .comments(comments)
                .viewCnt(currentViewCnt)
                .build());
    }


    @Transactional
    @Override
    public void delete(Long boardId) {
        Talk talk = talkRepository.findOne(boardId).orElseThrow(()
                -> new BoardNotFoundException(BOARD_NOT_FOUND.getMessage()));
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        if (!loggedInUserId.equals(talk.getWriter().getId()) && !UserRole.ROLE_ADMIN.equals(talk.getWriter().getRole())) {
            throw new ForbiddenException();
        }
        talkRepository.delete(boardId);
    }

}
