package com.pi.stepup.domain.music.api;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.service.MusicService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicApiController {
    private final MusicService musicService;

    @PostMapping
    public ResponseEntity<?> createMusic(@RequestBody MusicSaveRequestDto musicSaveRequestDto){
        Long musicId = musicService.create(musicSaveRequestDto).getMusicId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            "노래 등록 완료",
            musicService.readOne(musicId)
        ));
    }

    @GetMapping("/{musicId}")
    public ResponseEntity<?> readOneMusic(@PathVariable("musicId") Long musicId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                "노래 조회 완료",
                musicService.readOne(musicId)
        ));
    }
}
