package com.pi.stepup.domain.music.api;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.service.MusicService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicApiController {
    private final MusicService musicService;

    @PostMapping
    public ResponseEntity<?> createMusic(@RequestBody MusicSaveRequestDto music){
        Long musicId = musicService.create(music).getMusicId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            "노래 등록 완료",
            musicId
        ));
    }
}
