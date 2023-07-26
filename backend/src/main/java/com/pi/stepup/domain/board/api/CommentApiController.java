package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.comment.CommentRequestDto.CommentSaveRequestDto;
import com.pi.stepup.domain.board.service.comment.CommentService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/comment")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<ResponseDto<?>> createComment(@PathVariable Long boardId, @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
        commentSaveRequestDto.setBoardId(boardId);
        commentService.create(commentSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_COMMENT.getMessage()
        ));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto<?>> deleteComment(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_COMMENT.getMessage()
        ));
    }
}
