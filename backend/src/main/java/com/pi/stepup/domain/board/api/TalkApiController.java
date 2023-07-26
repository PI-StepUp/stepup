package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkSaveRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkUpdateRequestDto;
import com.pi.stepup.domain.board.service.talk.TalkService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class TalkApiController {

    private final TalkService talkService;

    @PostMapping("/talk")
    public ResponseEntity<ResponseDto<?>> createTalk(@RequestBody TalkSaveRequestDto talkSaveRequestDto) {

        talkService.create(talkSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_TALK.getMessage()
        ));
    }


    @PutMapping("/talk")
    public ResponseEntity<ResponseDto<?>> updateTalk(@RequestBody TalkUpdateRequestDto talkUpdateRequestDto) {

        talkService.update(talkUpdateRequestDto).getBoardId();

        return ResponseEntity.ok().body(ResponseDto.create(
                BoardResponseMessage.UPDATE_TALK.getMessage()
        ));
    }

    @GetMapping("/talk/{boardId}")
    public ResponseEntity<ResponseDto<?>> readOneTalk(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ONE_TALK.getMessage(),
                        talkService.readOne(boardId)
                )
        );
    }


    @GetMapping("/talk")
    public ResponseEntity<ResponseDto<?>> readAllTalk(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ALL_TALK.getMessage(),
                        talkService.readAll(keyword)
                )
        );
    }


    @DeleteMapping("talk/{boardId}")
    public ResponseEntity<ResponseDto<?>> deleteTalk(@PathVariable("boardId") Long boardId) {
        talkService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_TALK.getMessage()
        ));
    }

}
