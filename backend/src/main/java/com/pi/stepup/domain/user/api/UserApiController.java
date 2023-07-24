package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_ID_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.LOGIN_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ONE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.SIGN_UP_SUCCESS;

import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.LoginRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.SignUpRequestDto;
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

    @PostMapping("/dupid")
    public ResponseEntity<ResponseDto<?>> checkIdDuplicated(
        @RequestBody CheckIdRequestDto checkIdRequestDto) {
        userService.checkIdDuplicated(checkIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(CHECK_ID_DUPLICATED_SUCCESS.getMessage())
        );
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDto.create(
                SIGN_UP_SUCCESS.getMessage(),
                userService.signUp(signUpRequestDto)
            )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<?>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                LOGIN_SUCCESS.getMessage(),
                userService.login(loginRequestDto)
            )
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readOne(String id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                READ_ONE_SUCCESS.getMessage(),
                userService.readOne(id)
            )
        );
    }
}
