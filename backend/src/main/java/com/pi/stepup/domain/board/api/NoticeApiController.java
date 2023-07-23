package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;

//    @PostMapping("/notice")
}
