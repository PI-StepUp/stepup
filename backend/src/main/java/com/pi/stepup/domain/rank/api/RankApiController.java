package com.pi.stepup.domain.rank.api;

import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.service.PointHistoryService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pi.stepup.domain.rank.constant.RankResponseMessage.READ_PONT_HISTORY_SUCCESS;
import static com.pi.stepup.domain.rank.constant.RankResponseMessage.UPDATE_POINT_SUCCESS;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
@Slf4j
public class RankApiController {
    private final PointHistoryService pointHistoryService;

    @PostMapping("/point")
    public ResponseEntity<ResponseDto<?>> updatePoint(@RequestBody PointUpdateRequestDto pointUpdateRequestDto) {
        pointHistoryService.update(pointUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                UPDATE_POINT_SUCCESS.getMessage()
        ));
    }

    @GetMapping("/my/history/{id}")
    public ResponseEntity<ResponseDto<?>> readAllPointHistory(@PathVariable("id") String id) {
        log.info("포인트 적립 내역 : {}", pointHistoryService.readAll());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_PONT_HISTORY_SUCCESS.getMessage(),
                pointHistoryService.readAll()
        ));
    }
}
