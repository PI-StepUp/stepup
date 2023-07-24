package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class CommentApiController {

    private final CommentServiceImpl commentService;

}
