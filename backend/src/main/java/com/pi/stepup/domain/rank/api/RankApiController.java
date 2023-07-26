package com.pi.stepup.domain.rank.api;

import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.service.PointHistoryService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pi.stepup.domain.rank.constant.RankResponseMessage.UPDATE_POINT_SUCCESS;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankApiController {
    private final PointHistoryService pointHistoryService;

    @PostMapping("/point")
    public ResponseEntity<ResponseDto<?>> updatePoint(@RequestBody PointUpdateRequestDto pointUpdateRequestDto) {
        pointHistoryService.update(pointUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                UPDATE_POINT_SUCCESS.getMessage()
        ));
    }
}
