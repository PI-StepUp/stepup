package com.pi.stepup.domain.music.api;

import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.service.MusicApplyService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pi.stepup.domain.music.constant.MusicApplyResponseMessage.*;

@RestController
@RequestMapping("/api/music/apply")
@RequiredArgsConstructor
public class MusicApplyApiController {
    private final MusicApplyService musicApplyService;

    @PostMapping
    public ResponseEntity<?> createMusicApply(@RequestBody MusicApplySaveRequestDto musicApplySaveRequestDto) {
        Long musicApplyId = musicApplyService.create(musicApplySaveRequestDto).getMusicApplyId();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.create(
                        CREATE_MUSIC_APPLY_SUCCESS.getMessage(),
                        musicApplyService.readOne(musicApplyId)
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> readAllMusicApply(
            @RequestParam(required = false, name = "keyword") String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_ALL_MUSIC_APPLY_SUCCESS.getMessage(),
                musicApplyService.readAll(keyword)
        ));
    }

    @GetMapping("/{musicRequestId}")
    public ResponseEntity<?> readOneMusicApply(@PathVariable("musicRequestId") Long musicRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_ONE_MUSIC_APPLY_SUCCESS.getMessage(),
                musicApplyService.readOne(musicRequestId)
        ));
    }

    @DeleteMapping("/{musicRequestId}")
    public ResponseEntity<?> deleteMusicApply(@PathVariable("musicRequestId") Long musicRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                DELETE_MUSIC_APPLY_SUCCESS.getMessage()
        ));
    }
}
