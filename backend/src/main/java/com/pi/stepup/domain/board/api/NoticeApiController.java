package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.service.NoticeService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    public ResponseEntity<?> createNotice(@RequestBody NoticeSaveRequestDto noticeSaveRequestDto) {

        //Long boarId = noticeService.create(noticeSaveRequestDto).getBoardId();
        noticeService.create(noticeSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_NOTICE.getMessage()
        ));
    }


    @PutMapping("/notice")
    public ResponseEntity<?> updateNotice(@RequestBody NoticeUpdateRequestDto noticeUpdateRequestDto) {

        noticeService.update(noticeUpdateRequestDto).getBoardId();

        return ResponseEntity.ok().body(ResponseDto.create(
                BoardResponseMessage.UPDATE_NOTICE.getMessage()
        ));
    }

    @GetMapping("/notice/{boardId}")
    public ResponseEntity<ResponseDto<?>> readOneNotice(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ONE_NOTICE.getMessage(),
                        noticeService.readOne(boardId)
                )
        );
    }


    @GetMapping("/notice")
    public ResponseEntity<ResponseDto<?>> readAllNotice(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ALL_NOTICE.getMessage(),
                        noticeService.readAll(keyword)
                )
        );
    }


    @DeleteMapping("notice/{boardId}")
    public ResponseEntity<?> deleteNotice(@PathVariable("boardId") Long boardId) {
        noticeService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_NOTICE.getMessage()
        ));
    }
}
