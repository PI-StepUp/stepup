package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;

import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.service.UserService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/country")
    public ResponseEntity<ResponseDto<?>> readAllCountries() {
        return ResponseEntity.ok(
            ResponseDto.create(
                READ_ALL_COUNTRIES_SUCCESS.getMessage(),
                userService.readAllCountries()
            )
        );
    }

    @PostMapping("/dupemail")
    public ResponseEntity<ResponseDto<?>> checkEmailDuplicated(
        @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        userService.checkEmailDuplicated(checkEmailRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(CHECK_EMAIL_DUPLICATED_SUCCESS.getMessage())
        );
    }

    @PostMapping("/dupnick")
    public ResponseEntity<ResponseDto<?>> checkNicknameDuplicated(
        @RequestBody CheckNicknameRequestDto checkNicknameRequestDto) {
        userService.checkNicknameDuplicated(checkNicknameRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(CHECK_NICKNAME_DUPLICATED_SUCCESS.getMessage())
        );
    }
}
