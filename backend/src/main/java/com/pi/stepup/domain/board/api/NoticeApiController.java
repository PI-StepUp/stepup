package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeSaveRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto.NoticeUpdateRequestDto;
import com.pi.stepup.domain.board.service.notice.NoticeService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "notice", description = "notice domain apis")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 게시글 작성", description = "관리자만 정모 게시글을 작성한다. 사이트 자체 랜덤 댄스 개최 공지가 가능하다.")
    @ApiResponse(responseCode = "201", description = "공지사항 등록 완료")
    @PostMapping("/notice")
    public ResponseEntity<ResponseDto<?>> createNotice(@RequestBody @Valid NoticeSaveRequestDto noticeSaveRequestDto) {

        noticeService.create(noticeSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_NOTICE.getMessage()
        ));
    }


    @Operation(summary = "공지사항 게시글 수정",
            description = "관리자가 작성한 공지 게시글 제목, 내용, 이미지 파일, 개최한 랜덤 댄스를 수정한다.")
    @ApiResponse(responseCode = "200", description = "공지사항 수정 완료")
    @PutMapping("/notice")
    public ResponseEntity<ResponseDto<?>> updateNotice(@RequestBody @Valid NoticeUpdateRequestDto noticeUpdateRequestDto) {

        noticeService.update(noticeUpdateRequestDto).getBoardId();

        return ResponseEntity.ok().body(ResponseDto.create(
                BoardResponseMessage.UPDATE_NOTICE.getMessage()
        ));
    }

    @Operation(summary = "공지사항 게시글 상세 조회",
            description = "공지사항 게시글 상세 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "공지사항 게시글 조회 완료")
    @GetMapping("/notice/{boardId}")
    public ResponseEntity<ResponseDto<?>> readOneNotice(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ONE_NOTICE.getMessage(),
                        noticeService.readOne(boardId)
                )
        );
    }

    @Operation(summary = "공지사항 게시글 목록 조회", description = "저장되어 있는 모든 공지사항 게시글의 목록을 조회한다. 검색 기능도 가능하다.")
    @ApiResponse(responseCode = "200",
            description = "공지사항 전체 목록 조회 완료")
    @GetMapping("/notice")
    public ResponseEntity<ResponseDto<?>> readAllNotice(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ALL_NOTICE.getMessage(),
                        noticeService.readAll(keyword)
                )
        );
    }

    @Operation(summary = "공지사항 게시글 삭제", description = "관리자만 공지사항 게시글 삭제가 가능하다.")
    @ApiResponse(responseCode = "200",
            description = "공지사항 삭제 완료")
    @DeleteMapping("notice/{boardId}")
    public ResponseEntity<ResponseDto<?>> deleteNotice(@PathVariable("boardId") Long boardId) {
        noticeService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_NOTICE.getMessage()
        ));
    }
}
