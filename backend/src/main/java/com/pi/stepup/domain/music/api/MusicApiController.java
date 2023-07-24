package com.pi.stepup.domain.music.api;

import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.service.MusicService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pi.stepup.domain.music.constant.MusicResponseMessage.*;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicApiController {
    private final MusicService musicService;

    @PostMapping
    public ResponseEntity<?> createMusic(@RequestBody MusicSaveRequestDto musicSaveRequestDto) {
        Long musicId = musicService.create(musicSaveRequestDto).getMusicId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                CREATE_MUSIC_SUCCESS.getMessage(),
                musicService.readOne(musicId)
        ));
    }

    @GetMapping("/{musicId}")
    public ResponseEntity<?> readOneMusic(@PathVariable("musicId") Long musicId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_ONE_MUSIC_SUCCESS.getMessage(),
                musicService.readOne(musicId)
        ));
    }

    @GetMapping(params = "keyword")
    public ResponseEntity<?> readAllMusic(
            @RequestParam(required = false, name = "keyword") String keyword) {
        List<MusicFindResponseDto> result;
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                READ_ALL_MUSIC_SUCCESS.getMessage(),
                musicService.readAll(keyword)
        ));
    }

    @DeleteMapping("/{musicId}")
    public ResponseEntity<?> deleteMusic(@PathVariable("musicId") Long musicId) {
        musicService.delete(musicId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                DELETE_MUSIC_SUCCESS.getMessage()
        ));
    }
}
