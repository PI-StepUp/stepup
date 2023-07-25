package com.pi.stepup.domain.dance.api;

import com.pi.stepup.domain.dance.constant.DanceResponseMessage;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.service.DanceService;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dance")
@RequiredArgsConstructor
public class DanceApiController {

    private final DanceService danceService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createDance
            (@RequestBody DanceCreateRequestDto danceCreateRequestDto) {
        RandomDance createDance = danceService.createDance(danceCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                DanceResponseMessage.CREATE_RANDOM_DANCE.getMessage(),
                danceService.readDance(createDance.getRandomDanceId())
        ));
    }

    @PutMapping("/my")
    public ResponseEntity<ResponseDto<?>> updateDance
            (@RequestBody DanceUpdateRequestDto danceUpdateRequestDto) {
        RandomDance updateDance = danceService.updateDance(danceUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                DanceResponseMessage.UPDATE_CREATED_RANDOM_DANCE.getMessage(),
                danceService.readDance(updateDance.getRandomDanceId())
        ));
    }

    @DeleteMapping("/my/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> deleteDance
            (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.deleteDance(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                DanceResponseMessage.DELETE_CREATED_RANDOM_DANCE.getMessage()
        ));
    }

    @GetMapping("/playlist/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> readAllDanceMusic
            (@PathVariable("randomDanceId") Long randomDanceId) {
        List<Music> allMusic = danceService.readAllDanceMusic(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                DanceResponseMessage.SELECT_ALL_MUSIC.getMessage(),
                allMusic
        ));
    }

    @GetMapping("/my/opened/{id}")
    public ResponseEntity<ResponseDto<?>> readAllMyHeldDance
            (@PathVariable("id") String id) {
        List<RandomDance> allHeldDance = danceService.readAllMyHeldDance(id);

        List<DanceFindResponseDto> danceFindResponseDtoList = new ArrayList<>();
        for (int i = 0; i < allHeldDance.size(); i++) {
            DanceFindResponseDto danceFindResponseDto
                    = DanceFindResponseDto.builder().randomDance(allHeldDance.get(i)).build();
            danceFindResponseDtoList.add(danceFindResponseDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                DanceResponseMessage.SELECT_HELD_RANDOM_DANCE.getMessage(),
                danceFindResponseDtoList
        ));
    }

}

