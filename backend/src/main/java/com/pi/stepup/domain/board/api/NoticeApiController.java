package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.dto.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.service.NoticeService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    public ResponseEntity<ResponseDto> create(@RequestBody NoticeSaveRequestDto noticeSaveRequestDto) {
        noticeService.create(noticeSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                "공지 등록 완료",
                null
        ));
    }


//    @PutMapping("/notice/{boardId}")
//    public ResponseEntity<?> updateNotice(@PathVariable Long boardId, @RequestBody NoticeUpdateRequestDto noticeUpdateRequestDto) {
//        Long updatedBoardId = noticeService.update(boardId, noticeUpdateRequestDto);
//        return ResponseEntity.ok().body(ResponseDto.create("게시글 수정 완료", updatedBoardId));
//    }
}
