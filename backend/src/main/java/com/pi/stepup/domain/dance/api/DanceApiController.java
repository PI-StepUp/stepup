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
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_IN_PROGRESS_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.SELECT_SCHEDULED_RANDOM_DANCE;
import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.UPDATE_OPEN_RANDOM_DANCE;

import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.dance.service.DanceService;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "dance", description = "dance domain apis")
@RestController
@RequestMapping("/api/dance")
@RequiredArgsConstructor
public class DanceApiController {

    private final DanceService danceService;

    @Operation(summary = "랜덤 플레이 댄스 개최", description = "회원들이 참여할 랜덤 플레이 댄스를 개최한다. 유형은 서바이벌, 랭킹, 자율 중에서 고른다.")
    @ApiResponse(responseCode = "201", description = "랜덤 플레이 댄스 생성 완료")
    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createDance
        (@RequestBody @Valid DanceCreateRequestDto danceCreateRequestDto) {
        danceService.create(danceCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            CREATE_RANDOM_DANCE.getMessage()
        ));
    }

    @Operation(summary = "개최 랜덤 플레이 댄스 수정",
        description = "내가 개최한 랜덤 플레이 댄스 제목, 내용, 시작 및 끝 시간, 유형, 최대 참가 인원, 썸네일 이미지를 수정한다.")
    @ApiResponse(responseCode = "200", description = "랜덤 플레이 댄스 수정 완료")
    @PutMapping("/my")
    public ResponseEntity<ResponseDto<?>> updateDance
        (@RequestBody @Valid DanceUpdateRequestDto danceUpdateRequestDto) {
        danceService.update(danceUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            UPDATE_OPEN_RANDOM_DANCE.getMessage()
        ));
    }

    @Operation(summary = "개최 랜덤 플레이 댄스 삭제",
        description = "내가 개최한 랜덤 플레이 댄스를 삭제한다. 예약자가 있더라도 삭제할 수 있다. 종료된 랜덤 플레이 댄스는 삭제할 수 없다.")
    @ApiResponse(responseCode = "200", description = "랜덤 플레이 댄스 삭제 완료")
    @DeleteMapping("/my/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> deleteDance
        (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.delete(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_OPEN_RANDOM_DANCE.getMessage()
        ));
    }

    @Operation(summary = "개최 랜덤 플레이 댄스 사용 노래 목록 조회",
        description = "내가 개최한 랜덤 플레이 댄스에 사용한 노래 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "랜덤 플레이 댄스 노래 목록 조회 완료")
    @GetMapping("/playlist/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> readAllDanceMusic
        (@PathVariable("randomDanceId") Long randomDanceId) {
        List<MusicFindResponseDto> allDanceMusic = danceService.readAllDanceMusic(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_DANCE_MUSIC.getMessage(),
            allDanceMusic
        ));
    }

    @Operation(summary = "개최 랜덤 플레이 댄스 목록 조회",
        description = "내가 개최한 랜덤 플레이 댄스 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "내가 개최한 랜덤 플레이 댄스 목록 조회 완료")
    @GetMapping("/my/open")
    public ResponseEntity<ResponseDto<?>> readAllMyOpenDance() {
        List<DanceFindResponseDto> allMyOpenDance = danceService.readAllMyOpenDance();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_OPEN_RANDOM_DANCE.getMessage(),
            allMyOpenDance
        ));
    }

    @Operation(summary = "참여 가능한 랜덤 플레이 댄스 목록 조회",
        description = "현재 시간과 비교해 참여 가능한 랜덤 플레이 댄스 목록을 조회한다. 인기순(예약자 및 참여자 많은 순), 시작 시간이 현재 시간과 가까운 순으로 정렬한다.")
    @ApiResponse(responseCode = "200", description = "참여 가능한 랜덤 플레이 댄스 목록 조회 완료")
    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readAllRandomDance
        (DanceSearchRequestDto danceSearchRequestDto) {
        List<DanceSearchResponseDto> allDance
            = danceService.readAllRandomDance(danceSearchRequestDto);

        if (danceSearchRequestDto.getProgressType().equals(ProgressType.SCHEDULED.toString())) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                SELECT_SCHEDULED_RANDOM_DANCE.getMessage(),
                allDance
            ));
        } else if (danceSearchRequestDto.getProgressType()
            .equals(ProgressType.IN_PROGRESS.toString())) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                SELECT_IN_PROGRESS_RANDOM_DANCE.getMessage(),
                allDance
            ));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                SELECT_ALL_RANDOM_DANCE.getMessage(),
                allDance
            ));
        }
    }

    @Operation(summary = "랜덤 플레이 댄스 예약",
        description = "참여하고 싶은 랜덤 플레이 댄스를 예약한다.")
    @ApiResponse(responseCode = "201", description = "랜덤 플레이 댄스 예약 완료")
    @PostMapping("/reserve/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> createReservation
        (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.createReservation(randomDanceId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            RESERVE_RANDOM_DANCE.getMessage()
        ));
    }

    @Operation(summary = "랜덤 플레이 댄스 예약 취소",
        description = "내가 예약한 랜덤 플레이 댄스의 예약을 취소한다.")
    @ApiResponse(responseCode = "200", description = "랜덤 플레이 댄스 예약 취소 완료")
    @DeleteMapping("/my/reserve/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> deleteReservation
        (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.deleteReservation(randomDanceId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_RESERVE_RANDOM_DANCE.getMessage()
        ));
    }

    @Operation(summary = "예약 랜덤 플레이 댄스 목록 조회",
        description = "내가 예약한 랜덤 플레이 댄스 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "내가 예약한 랜덤 플레이 댄스 목록 조회 완료")
    @GetMapping("/my/reserve")
    public ResponseEntity<ResponseDto<?>> readAllMyReserveDance() {
        List<DanceFindResponseDto> allMyRandomDance
            = danceService.readAllMyReserveDance();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_RESERVE_RANDOM_DANCE.getMessage(),
            allMyRandomDance
        ));
    }

    @Operation(summary = "랜덤 플레이 댄스 참여",
        description = "참여하고 싶은 랜덤 플레이 댄스에 참여한다.")
    @ApiResponse(responseCode = "201", description = "랜덤 플레이 댄스 참여 완료")
    @PostMapping("/attend/{randomDanceId}")
    public ResponseEntity<ResponseDto<?>> createAttend
        (@PathVariable("randomDanceId") Long randomDanceId) {
        danceService.createAttend(randomDanceId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            ATTEND_RANDOM_DANCE.getMessage()
        ));
    }

    @Operation(summary = "참여 랜덤 플레이 댄스 목록 조회",
        description = "내가 참여한 랜덤 플레이 댄스 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "내가 참여한 랜덤 플레이 댄스 목록 조회 완료")
    @GetMapping("/my/attend")
    public ResponseEntity<ResponseDto<?>> readAllMyAttendDance() {
        List<DanceFindResponseDto> allMyRandomDance
            = danceService.readAllMyAttendDance();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            SELECT_ALL_ATTEND_RANDOM_DANCE.getMessage(),
            allMyRandomDance
        ));
    }

}

