package com.pi.stepup.domain.music.api;

import static com.pi.stepup.domain.music.constant.MusicResponseMessage.CREATE_MUSIC_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicResponseMessage.DELETE_MUSIC_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicResponseMessage.READ_ALL_MUSIC_SUCCESS;
import static com.pi.stepup.domain.music.constant.MusicResponseMessage.READ_ONE_MUSIC_SUCCESS;

import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.service.MusicService;
import com.pi.stepup.global.dto.ResponseDto;
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

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicApiController {

    private final MusicService musicService;

    @PostMapping
    public ResponseEntity<ResponseDto<?>> createMusic(
        @RequestBody MusicSaveRequestDto musicSaveRequestDto) {
        Long musicId = musicService.create(musicSaveRequestDto).getMusicId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
            CREATE_MUSIC_SUCCESS.getMessage(),
            musicService.readOne(musicId)
        ));
    }

    @GetMapping("/{musicId}")
    public ResponseEntity<ResponseDto<?>> readOneMusic(@PathVariable("musicId") Long musicId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ONE_MUSIC_SUCCESS.getMessage(),
            musicService.readOne(musicId)
        ));
    }

    @GetMapping(params = "keyword")
    public ResponseEntity<ResponseDto<?>> readAllMusic(
        @RequestParam(required = false, name = "keyword") String keyword) {
        List<MusicFindResponseDto> result;
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            READ_ALL_MUSIC_SUCCESS.getMessage(),
            musicService.readAll(keyword)
        ));
    }

    @DeleteMapping("/{musicId}")
    public ResponseEntity<ResponseDto<?>> deleteMusic(@PathVariable("musicId") Long musicId) {
        musicService.delete(musicId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
            DELETE_MUSIC_SUCCESS.getMessage()
        ));
    }
}
