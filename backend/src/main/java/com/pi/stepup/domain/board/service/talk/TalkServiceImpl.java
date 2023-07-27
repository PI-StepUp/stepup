package com.pi.stepup.domain.board.service.talk;

import com.pi.stepup.domain.board.dao.talk.TalkRepository;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkSaveRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkUpdateRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkResponseDto.TalkInfoResponseDto;
import com.pi.stepup.domain.board.service.comment.CommentService;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalkServiceImpl implements TalkService {

    private final TalkRepository talkRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Transactional
    @Override
    public Talk create(TalkSaveRequestDto talkSaveRequestDto) {
        User writer = userRepository.findById(talkSaveRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID: " + talkSaveRequestDto.getId()));

        Talk talk = Talk.builder()
                .writer(writer)
                .title(talkSaveRequestDto.getTitle())
                .content(talkSaveRequestDto.getTitle())
                .fileURL(talkSaveRequestDto.getFileURL())
                .build();

        talkRepository.insert(talk);

        return talk;
    }

    @Transactional
    @Override
    public Talk update(TalkUpdateRequestDto talkUpdateRequestDto) {
        Talk talk = talkRepository.findOne(talkUpdateRequestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        talk.update(talkUpdateRequestDto.getTitle(), talkUpdateRequestDto.getContent(),
                talkUpdateRequestDto.getFileURL());

        return talk;
    }

    @Transactional
    @Override
    public List<TalkInfoResponseDto> readAll(String keyword) {
        List<Talk> allTalks = talkRepository.findAll(keyword);
        return allTalks.stream()
                .map(t -> TalkInfoResponseDto.builder().talk(t).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TalkInfoResponseDto> readAllById(String id) {
        List<Talk> allMyTalks = talkRepository.findById(id);
        return allMyTalks.stream()
                .map(m -> TalkInfoResponseDto.builder().talk(m).build())
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public Optional<TalkInfoResponseDto> readOne(Long boardId) {
        List<CommentInfoResponseDto> comments = commentService.readByBoardId(boardId);
        return Optional.ofNullable(TalkInfoResponseDto.builder()
                .talk(talkRepository.findOne(boardId).orElseThrow())
                .comments(comments)
                .build());
    }

    @Transactional
    @Override
    public void delete(Long boardId) {
        talkRepository.delete(boardId);
    }

}
