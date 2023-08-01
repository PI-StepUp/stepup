package com.pi.stepup.domain.rank.api;

import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.service.PointHistoryService;
import com.pi.stepup.domain.rank.service.RankService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pi.stepup.domain.rank.constant.RankResponseMessage.*;

@Tag(name = "rank", description = "rank domain apis")
@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
@Slf4j
public class RankApiController {
    private final PointHistoryService pointHistoryService;
    private final RankService rankService;

    @Operation(summary = "포인트 적립",
        description = "사용자가 랜플댄에 참여하여 수상하거나 노래를 맞힐 경우, 랜플댄 개최, 연습실 첨여할 경우 포인트를 적립한다.")
    @ApiResponse(responseCode = "200",
        description = "포인트 적립 완료")
    @PostMapping("/point")
    public ResponseEntity<ResponseDto<?>> updatePoint(@RequestBody PointUpdateRequestDto pointUpdateRequestDto) {
        pointHistoryService.update(pointUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                UPDATE_POINT_SUCCESS.getMessage()
        ));
    }

    @Operation(summary = "포인트 적립 내역 조회",
        description = "사용자의 포인트 적립 내역들을 상세히 보여준다.")
    @ApiResponse(responseCode = "200",
        description = "포인트 적립 내역 조회 완료")
    @GetMapping("/my/history")
    public ResponseEntity<ResponseDto<?>> readAllPointHistory() {
        log.info("포인트 적립 내역 : {}", pointHistoryService.readAll());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_PONT_HISTORY_SUCCESS.getMessage(),
                pointHistoryService.readAll()
        ));
    }

    @Operation(summary = "포인트 조회",
        description = "사용자의 포인트를 조회한다.")
    @ApiResponse(responseCode = "200",
        description = "포인트 조회 완료")
    @GetMapping("/my/point")
    public ResponseEntity<ResponseDto<?>> readPoint() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_POINT_SUCCESS.getMessage(),
                pointHistoryService.readPoint()
        ));
    }

    @Operation(summary = "사용자 등급 조회",
        description = "사용자의 등급을 조회한다.")
    @ApiResponse(responseCode = "200",
        description = "사용자 등급 조회 완료")
    @GetMapping("/my/grade")
    public ResponseEntity<ResponseDto<?>> readUserRank() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_RANK_SUCCESS.getMessage(),
                rankService.readOne()
        ));
    }
}
