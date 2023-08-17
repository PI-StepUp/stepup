package com.pi.stepup.domain.music.api;

import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.ADD_MUSIC_APPLY_LIKE_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.CREATE_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.DELETE_MUSIC_APPLY_LIKE_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.DELETE_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_ALL_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_MUSIC_APPLY_HEART_STATUS_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_MY_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_ONE_MUSIC_APPLY_SUCCESS;

import com.pi.stepup.domain.music.dto.MusicRequestDto.HeartSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.service.MusicApplyService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "music", description = "music domain apis")
@RestController
@RequestMapping("/api/music/apply")
@RequiredArgsConstructor
public class MusicApplyApiController {

    private final MusicApplyService musicApplyService;

    @Operation(summary = "노래 신청 등록", description = "사용자가 신청하고 싶은 노래를 등록한다.")
    @ApiResponse(responseCode = "201",
        description = "노래 신청 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> createMusicApply(
        @RequestBody MusicApplySaveRequestDto musicApplySaveRequestDto) {
        musicApplyService.create(musicApplySaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDto.create(
                CREATE_MUSIC_APPLY_SUCCESS.getMessage()
            )
        );
    }

    @Operation(summary = "노래 신청 목록 조회",
        description = "등록되어 있는 모든 노래 신청들의 상세 정보를 불러온다.")
    @ApiResponse(responseCode = "200",
        description = "노래 신청 목록 조회 완료")
    @GetMapping
    public ResponseEntity<ResponseDto<?>> readAllByKeywordMusicApply(
        @RequestParam(required = false, name = "keyword") String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ALL_MUSIC_APPLY_SUCCESS.getMessage(),
            musicApplyService.readAllByKeyword(keyword)
        ));
    }

    @Operation(summary = "노래 신청 목록 조회 (마이페이지)",
        description = "현재 접속 해 있는 유저가 신청한 노래들의 상세 정보를 불러온다.")
    @ApiResponse(responseCode = "200",
        description = "나의 노래 신청 목록 조회 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @ApiResponse(responseCode = "403",
        description = "접근 권한 없음")
    @GetMapping("/my")
    public ResponseEntity<ResponseDto<?>> readAllByIdMusicApply() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_MY_MUSIC_APPLY_SUCCESS.getMessage(),
            musicApplyService.readAllById()
        ));
    }

    @Operation(summary = "노래 신청 상세 조회", description = "사용자가 신청한 노래의 상세 정보를 불러온다.")
    @ApiResponse(responseCode = "200",
        description = "노래 신청 상세 조회 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @GetMapping("/detail/{musicApplyId}")
    public ResponseEntity<ResponseDto<?>> readOneMusicApply(
        @PathVariable(name = "musicApplyId") Long musicApplyId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ONE_MUSIC_APPLY_SUCCESS.getMessage(),
            musicApplyService.readOne(musicApplyId)
        ));
    }

    @Operation(summary = "노래 신청 삭제",
        description = "사용자는 자신이 작성한 신청 글을 삭제 할 수 있다.")
    @ApiResponse(responseCode = "200",
        description = "노래 신청 삭제 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @ApiResponse(responseCode = "403",
        description = "접근 권한 없음")
    @ApiResponse(responseCode = "400",
        description = "노래 신청 조회 실패")
    @DeleteMapping("/{musicApplyId}")
    public ResponseEntity<ResponseDto<?>> deleteMusicApply(
        @PathVariable("musicApplyId") Long musicApplyId) {
        musicApplyService.delete(musicApplyId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_MUSIC_APPLY_SUCCESS.getMessage()
        ));
    }

    @Operation(summary = "노래 신청 좋아요",
        description = "사용자의 해당하는 노래 신청 좋아요 상태가 1 이면 좋아요를 추가 할 수 있다.")
    @ApiResponse(responseCode = "201",
        description = "노래 신청 좋아요 완료")
    @ApiResponse(responseCode = "400",
        description = "1. 사용자 조회 실패 \t\n 2. 노래 신청 조회 실패")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @PostMapping("/heart")
    public ResponseEntity<ResponseDto<?>> addMusicApplyHeart(
        @RequestBody HeartSaveRequestDto heartSaveRequestDto) {
        musicApplyService.createHeart(heartSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            ADD_MUSIC_APPLY_LIKE_SUCCESS.getMessage()
        ));
    }


    @Operation(summary = "노래 신청 좋아요 취소",
        description = "사용자의 해당하는 노래 신청 좋아요 상태가 0 이면 좋아요를 취소 할 수 있다.")
    @ApiResponse(responseCode = "200",
        description = "좋아요 취소 완료")
    @ApiResponse(responseCode = "400",
        description = "좋아요 취소 실패")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @DeleteMapping("/heart/{musicApplyId}")
    public ResponseEntity<ResponseDto<?>> deleteMusicApplyHeart(
        @PathVariable(name = "musicApplyId") Long musicApplyId) {
        musicApplyService.deleteHeart(musicApplyId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_MUSIC_APPLY_LIKE_SUCCESS.getMessage()
        ));
    }

    @Operation(summary = "노래 신청 좋아요 상태",
        description = "사용자의 해당하는 노래 신청 좋아요 상태를 확인 할 수 있다.")
    @ApiResponse(responseCode = "200",
        description = "노래 신청 좋아요 상태 확인 완료")
    @ApiResponse(responseCode = "400",
        description = "좋아요 상태 확인 실패")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @GetMapping("/heart/{musicApplyId}")
    public ResponseEntity<ResponseDto<?>> readMusicApplyHeartStatus(
        @PathVariable(name = "musicApplyId") Long musicApplyId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_MUSIC_APPLY_HEART_STATUS_SUCCESS.getMessage(),
            musicApplyService.findHeartStatus(musicApplyId)
        ));
    }
}
