package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentServiceImpl commentService;

}
