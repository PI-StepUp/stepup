package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkSaveRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkUpdateRequestDto;
import com.pi.stepup.domain.board.service.talk.TalkService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "talk", description = "talk domain apis")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders="*")
public class TalkApiController {

    private final TalkService talkService;

    @Operation(summary = "자유게시판 게시글 작성", description = "회원들이 자유게시판 게시글을 작성한다.")
    @ApiResponse(responseCode = "201", description = "자유게시판 등록 완료")
    @PostMapping("/talk")
    public ResponseEntity<ResponseDto<?>> createTalk(@RequestBody TalkSaveRequestDto talkSaveRequestDto) {

        talkService.create(talkSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_TALK.getMessage()
        ));
    }

    @Operation(summary = "자유게시판 게시글 수정",
            description = "내가 작성한 정모 게시글 제목, 내용, 이미지 파일을 수정한다.")
    @ApiResponse(responseCode = "200", description = "자유게시판 수정 완료")
    @PutMapping("/talk")
    public ResponseEntity<ResponseDto<?>> updateTalk(@RequestBody TalkUpdateRequestDto talkUpdateRequestDto) {

        talkService.update(talkUpdateRequestDto).getBoardId();

        return ResponseEntity.ok().body(ResponseDto.create(
                BoardResponseMessage.UPDATE_TALK.getMessage()
        ));
    }

    @Operation(summary = "자유게시판 게시글 상세 조회",
            description = "자유게시판 게시글 상세 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "자유게시판 게시글 조회 완료")
    @GetMapping("/talk/{boardId}")
    public ResponseEntity<ResponseDto<?>> readOneTalk(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ONE_TALK.getMessage(),
                        talkService.readOne(boardId)
                )
        );
    }

    @Operation(summary = "자유게시판 게시글 목록 조회", description = "저장되어 있는 모든 자유게시판 게시글의 목록을 조회한다. 검색 기능도 가능하다.")
    @ApiResponse(responseCode = "200",
            description = "자유게시판 목록 조회 완료")
    @GetMapping("/talk")
    public ResponseEntity<ResponseDto<?>> readAllTalk(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ALL_TALK.getMessage(),
                        talkService.readAll(keyword)
                )
        );
    }

    @Operation(summary = "내가 작성한 자유게시판 게시글 목록 조회", description = "내가 작성한 모든 자유게시판 게시글의 목록을 조회한다.")
    @ApiResponse(responseCode = "200",
            description = "내가 작성한 자유게시판 목록 조회")
    @GetMapping("talk/my")
    public ResponseEntity<ResponseDto<?>> readAllByIdTalk(
            @RequestParam(name = "id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.READ_ALL_MY_TALK.getMessage(),
                talkService.readAllById(id)
        ));
    }

    @Operation(summary = "자유게시판 게시글 삭제", description = "작성자만 자유게시판 게시글 삭제가 가능하다.")
    @ApiResponse(responseCode = "200",
            description = "자유게시판 삭제 완료")
    @DeleteMapping("talk/{boardId}")
    public ResponseEntity<ResponseDto<?>> deleteTalk(@PathVariable("boardId") Long boardId) {
        talkService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_TALK.getMessage()
        ));
    }

}
