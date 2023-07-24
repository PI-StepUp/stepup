package com.pi.stepup.domain.dance.api;

import com.pi.stepup.domain.dance.constant.DanceResponseMessage;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSaveRequestDto;
import com.pi.stepup.domain.dance.service.DanceService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dance")
@RequiredArgsConstructor
public class DanceApiController {

    private final DanceService danceService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createRandomDance
        (@RequestBody DanceSaveRequestDto danceSaveRequestDto) {
        RandomDance createDance = danceService.create(danceSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            DanceResponseMessage.CREATE_RANDOM_DANCE.getMessage(),
            danceService.readOne(createDance.getId())
        ));
    }

    @PutMapping("/my")
    public ResponseEntity<ResponseDto<?>> updateRandomDance
        (@RequestBody DanceSaveRequestDto danceSaveRequestDto) {
        RandomDance updateDance = danceService.update(danceSaveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DanceResponseMessage.UPDATE_CREATED_RANDOM_DANCE.getMessage(),
            danceService.readOne(updateDance.getId())
        ));
    }

    @DeleteMapping("/my/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> deleteRandomDance
        (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.delete(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DanceResponseMessage.DELETE_CREATED_RANDOM_DANCE.getMessage(),
            null
        ));
    }

}

