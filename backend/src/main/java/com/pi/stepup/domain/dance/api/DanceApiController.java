package com.pi.stepup.domain.dance.api;

import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.ATTEND_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.CREATE_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.DELETE_OPEN_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.DELETE_RESERVE_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.RESERVE_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_ALL_ATTEND_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_ALL_DANCE_MUSIC;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_ALL_OPEN_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_ALL_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_ALL_RESERVE_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.UPDATE_OPEN_RANDOM_DANCE;

import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceAttendRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceReserveRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.service.DanceService;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.global.dto.ResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dance")
@RequiredArgsConstructor
public class DanceApiController {

    private final DanceService danceService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createDance
        (@RequestBody DanceCreateRequestDto danceCreateRequestDto) {
        danceService.create(danceCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            CREATE_RANDOM_DANCE.getMessage()
        ));
    }

    @PutMapping("/my")
    public ResponseEntity<ResponseDto<?>> updateDance
        (@RequestBody DanceUpdateRequestDto danceUpdateRequestDto) {
        danceService.update(danceUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            UPDATE_OPEN_RANDOM_DANCE.getMessage()
        ));
    }

    @DeleteMapping("/my/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> deleteDance
        (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.delete(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_OPEN_RANDOM_DANCE.getMessage()
        ));
    }

    @GetMapping("/playlist/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> readAllDanceMusic
        (@PathVariable("randomDanceId") Long randomDanceId) {
        List<MusicFindResponseDto> allDanceMusic = danceService.readAllDanceMusic(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_DANCE_MUSIC.getMessage(),
            allDanceMusic
        ));
    }

    @GetMapping("/my/open")
    public ResponseEntity<ResponseDto<?>> readAllMyOpenDance
        (@RequestParam("id") String id) {
        List<DanceFindResponseDto> allMyOpenDance = danceService.readAllMyOpenDance(id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_OPEN_RANDOM_DANCE.getMessage(),
            allMyOpenDance
        ));
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readAllRandomDance
        (DanceSearchRequestDto danceSearchRequestDto) {

        //해당 되는 목록 조회
        List<DanceFindResponseDto> allDance
            = danceService.readAllRandomDance(danceSearchRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_RANDOM_DANCE.getMessage(),
            allDance
        ));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ResponseDto<?>> reserveDance
        (@RequestBody DanceReserveRequestDto danceReserveRequestDto) {
        danceService.createReservation(danceReserveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            RESERVE_RANDOM_DANCE.getMessage()
        ));
    }

    @DeleteMapping("/my/reserve")
    public ResponseEntity<ResponseDto<?>> unreserveDance
        (@RequestParam("randomDanceId") Long randomDanceId, @RequestParam("id") String id) {
        danceService.deleteReservation(randomDanceId, id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_RESERVE_RANDOM_DANCE.getMessage()
        ));
    }

    @GetMapping("/my/reserve")
    public ResponseEntity<ResponseDto<?>> readAllMyReserveDance
        (@RequestParam("id") String id) {

        List<DanceFindResponseDto> allMyRandomDance
            = danceService.readAllMyReserveDance(id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_RESERVE_RANDOM_DANCE.getMessage(),
            allMyRandomDance
        ));
    }

    @PostMapping("/attend")
    public ResponseEntity<ResponseDto<?>> reserveDance
        (@RequestBody DanceAttendRequestDto danceAttendRequestDto) {
        danceService.createAttend(danceAttendRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            ATTEND_RANDOM_DANCE.getMessage()
        ));
    }

    @GetMapping("/my/attend")
    public ResponseEntity<ResponseDto<?>> readAllMyAttendDance
        (@RequestParam("id") String id) {

        List<DanceFindResponseDto> allMyRandomDance
            = danceService.readAllMyAttendDance(id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_ATTEND_RANDOM_DANCE.getMessage(),
            allMyRandomDance
        ));
    }

}

