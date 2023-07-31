package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.service.comment.CommentService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "comment", description = "comment domain apis")
@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class CommentApiController {

    private final CommentService commentService;


    @Operation(summary = "댓글 등록", description = "게시글에 해당하는 댓글을 작성한다.")
    @ApiResponse(responseCode = "201", description = "댓글 등록 완료")

    @PostMapping("comment/{boardId}")
    public ResponseEntity<ResponseDto<?>> createComment(@PathVariable Long boardId, @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
        commentSaveRequestDto.setBoardId(boardId);
        commentService.create(commentSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_COMMENT.getMessage()
        ));
    }

    @Operation(summary = "댓글 삭제",
            description = "작성자만 댓글을 삭제한다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 완료")

    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<ResponseDto<?>> deleteComment(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_COMMENT.getMessage()
        ));
    }
}
