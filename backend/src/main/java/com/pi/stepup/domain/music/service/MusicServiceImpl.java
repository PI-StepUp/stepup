package com.pi.stepup.domain.music.service;

import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_ANSWER_NOT_FOUND;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_DELETE_FAIL;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_DUPLICATED;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_NOT_FOUND;

import com.pi.stepup.domain.music.dao.MusicAnswerRepository;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.domain.MusicAnswer;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicUpdateRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicDuplicatedException;
import com.pi.stepup.domain.music.exception.MusicNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;
    private final MusicAnswerRepository musicAnswerRepository;

    @Override
    @Transactional
    public Music create(MusicSaveRequestDto musicSaveRequestDto) {
        Music music = musicSaveRequestDto.toMusic();
        MusicAnswer musicAnswer = musicSaveRequestDto.toMusicAnswer();

        if (musicRepository.findByTitleAndArtist(music.getTitle(), music.getArtist()).isPresent()) {
            throw new MusicDuplicatedException(MUSIC_DUPLICATED.getMessage());
        }

        musicAnswerRepository.save(musicAnswer);
        music.setAnswerAsMusicAnswerId(musicAnswer.getId());

        return musicRepository.insert(music);
    }

    @Override
    @Transactional
    public void update(MusicUpdateRequestDto musicUpdateRequestDto) {
        Music music = musicRepository.findOne(musicUpdateRequestDto.getMusicId())
            .orElseThrow(() -> new MusicNotFoundException(MUSIC_NOT_FOUND.getMessage()));

        MusicAnswer musicAnswer = musicAnswerRepository.findById(music.getAnswer())
            .orElseThrow(() -> new MusicNotFoundException(MUSIC_ANSWER_NOT_FOUND.getMessage()));

        musicAnswer.updateMusicAnswer(musicUpdateRequestDto.getAnswer());
        musicAnswerRepository.save(musicAnswer);
        music.updateMusicInfo(musicUpdateRequestDto.toEntity());
    }

    @Override
    public MusicFindResponseDto readOne(Long musicId) {
        Music music = musicRepository.findOne(musicId)
            .orElseThrow(() -> new MusicNotFoundException(MUSIC_NOT_FOUND.getMessage()));
        MusicAnswer musicAnswer = musicAnswerRepository.findById(music.getAnswer())
            .orElseThrow(() -> new MusicNotFoundException(MUSIC_ANSWER_NOT_FOUND.getMessage()));
        return MusicFindResponseDto.builder()
            .music(music)
            .musicAnswer(musicAnswer)
            .build();
    }

    public List<MusicFindResponseDto> readAll(String keyword) {
        return musicRepository.findAll(keyword).stream()
            .map(music -> MusicFindResponseDto.builder()
                .music(music)
                .musicAnswer(musicAnswerRepository.findById(music.getAnswer())
                    .orElseThrow(
                        () -> new MusicNotFoundException(MUSIC_ANSWER_NOT_FOUND.getMessage())))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long musicId) {
        Music music = musicRepository.findOne(musicId)
            .orElseThrow(() -> new MusicNotFoundException(MUSIC_DELETE_FAIL.getMessage()));

        MusicAnswer musicAnswer = musicAnswerRepository.findById(music.getAnswer())
            .orElseThrow(() -> new MusicNotFoundException(MUSIC_ANSWER_NOT_FOUND.getMessage()));

        musicAnswerRepository.delete(musicAnswer);
        musicRepository.delete(musicId);
    }
}
