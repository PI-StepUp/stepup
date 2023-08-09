package com.pi.stepup.domain.music.api;

import static com.pi.stepup.domain.music.constant.MusicResponseMessage.CREATE_MUSIC_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicResponseMessage.DELETE_MUSIC_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicResponseMessage.READ_ALL_MUSIC_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicResponseMessage.READ_ONE_MUSIC_SUCCESS;

import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.service.MusicService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicApiController {

    private final MusicService musicService;

    @Operation(summary = "노래 등록", description = "관리자만 노래 등록이 가능하다.")
    @ApiResponse(responseCode = "201",
        description = "노래 등록 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @ApiResponse(responseCode = "403",
        description = "접근 권한 없음")
    @ApiResponse(responseCode = "409",
        description = "노래 중복")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> createMusic(
        @RequestBody MusicSaveRequestDto musicSaveRequestDto) {
        musicService.create(musicSaveRequestDto).getMusicId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            CREATE_MUSIC_SUCCESS.getMessage()
        ));
    }

    @Operation(summary = "노래 한 곡 조회", description = "노래의 상세 정보를 조회한다.")
    @ApiResponse(responseCode = "200",
        description = "노래 조회 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @ApiResponse(responseCode = "400",
        description = "노래 조회 실패")
    @GetMapping("/{musicId}")
    public ResponseEntity<ResponseDto<?>> readOneMusic(@PathVariable("musicId") Long musicId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ONE_MUSIC_SUCCESS.getMessage(),
            musicService.readOne(musicId)
        ));
    }

    @Operation(summary = "노래 목록 조회", description = "저장되어 있는 모든 노래들의 상세 정보를 조회한다.")
    @ApiResponse(responseCode = "200",
        description = "노래 목록 조회 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @GetMapping(params = "keyword")
    public ResponseEntity<ResponseDto<?>> readAllMusic(
        @RequestParam(required = false, name = "keyword") String keyword) {
        List<MusicFindResponseDto> result;
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ALL_MUSIC_SUCCESS.getMessage(),
            musicService.readAll(keyword)
        ));
    }

    @Operation(summary = "노래 삭제", description = "관리자만 노래 삭제가 가능하다.")
    @ApiResponse(responseCode = "200",
        description = "노래 삭제 완료")
    @ApiResponse(responseCode = "401",
        description = "인증 실패")
    @ApiResponse(responseCode = "403",
        description = "접근 권한 없음")
    @ApiResponse(responseCode = "400",
        description = "노래 삭제 실패")
    @DeleteMapping("/{musicId}")
    public ResponseEntity<ResponseDto<?>> deleteMusic(@PathVariable("musicId") Long musicId) {
        musicService.delete(musicId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_MUSIC_SUCCESS.getMessage()
        ));
    }
}
