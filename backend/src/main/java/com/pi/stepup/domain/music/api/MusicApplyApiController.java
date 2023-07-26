package com.pi.stepup.domain.music.api;

import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.ADD_MUSIC_APPLY_LIKE_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.CREATE_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.DELETE_MUSIC_APPLY_LIKE_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.DELETE_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_ALL_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_MY_MUSIC_APPLY_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.READ_ONE_MUSIC_APPLY_SUCCESS;

import com.pi.stepup.domain.music.dto.MusicRequestDto.HeartSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.service.MusicApplyService;
import com.pi.stepup.global.dto.ResponseDto;
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

@RestController
@RequestMapping("/api/music/apply")
@RequiredArgsConstructor
public class MusicApplyApiController {

    private final MusicApplyService musicApplyService;

    @PostMapping
    public ResponseEntity<ResponseDto<?>> createMusicApply(
        @RequestBody MusicApplySaveRequestDto musicApplySaveRequestDto) {
        Long musicApplyId = musicApplyService.create(musicApplySaveRequestDto).getMusicApplyId();

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDto.create(
                CREATE_MUSIC_APPLY_SUCCESS.getMessage(),
                musicApplyService.readOne(musicApplyId)
            )
        );
    }

    // TODO : 아래 두개(readAllBy~) 리팩토링 가능?
    @GetMapping
    public ResponseEntity<ResponseDto<?>> readAllByKeywordMusicApply(
        @RequestParam(required = false, name = "keyword") String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ALL_MUSIC_APPLY_SUCCESS.getMessage(),
            musicApplyService.readAllByKeyword(keyword)
        ));
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDto<?>> readAllByIdMusicApply(
        @RequestParam(name = "id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_MY_MUSIC_APPLY_SUCCESS.getMessage(),
            musicApplyService.readAllById(id)
        ));
    }

    @GetMapping("/{musicRequestId}")
    public ResponseEntity<ResponseDto<?>> readOneMusicApply(
        @PathVariable("musicRequestId") Long musicRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ONE_MUSIC_APPLY_SUCCESS.getMessage(),
            musicApplyService.readOne(musicRequestId)
        ));
    }

    @DeleteMapping("/{musicRequestId}")
    public ResponseEntity<ResponseDto<?>> deleteMusicApply(
        @PathVariable("musicRequestId") Long musicRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_MUSIC_APPLY_SUCCESS.getMessage()
        ));
    }

    @PostMapping("/heart")
    public ResponseEntity<ResponseDto<?>> addMusicApplyHeart(
        @RequestBody HeartSaveRequestDto heartSaveRequestDto) {
        musicApplyService.createHeart(heartSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            ADD_MUSIC_APPLY_LIKE_SUCCESS.getMessage()
        ));
    }

    @DeleteMapping("/heart")
    public ResponseEntity<ResponseDto<?>> deleteMusicApplyHeart(
        @RequestParam(name = "id") String id,
        @RequestParam(name = "musicApplyId") Long musicApplyId) {
        musicApplyService.deleteHeart(id, musicApplyId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_MUSIC_APPLY_LIKE_SUCCESS.getMessage()
        ));
    }
}
