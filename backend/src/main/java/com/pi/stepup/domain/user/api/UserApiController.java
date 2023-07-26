package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_ID_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_PASSWORD_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.DELETE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.FIND_ID_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.LOGIN_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ONE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.SIGN_UP_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.UPDATE_USER_SUCCESS;

import com.pi.stepup.domain.user.dto.UserRequestDto.AuthenticationRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.FindIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.SignUpRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.UpdateUserRequestDto;
import com.pi.stepup.domain.user.service.UserService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserApiController.class);

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
    public ResponseEntity<ResponseDto<?>> login(
        @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                LOGIN_SUCCESS.getMessage(),
                userService.login(authenticationRequestDto)
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

    @DeleteMapping("")
    public ResponseEntity<ResponseDto<?>> delete(String id) {
        userService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                DELETE_SUCCESS.getMessage()
            )
        );
    }

    @PostMapping("/checkpw")
    public ResponseEntity<ResponseDto<?>> checkPw(
        @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        userService.checkPassword(authenticationRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                CHECK_PASSWORD_SUCCESS.getMessage()
            )
        );
    }

    @PutMapping("")
    public ResponseEntity<ResponseDto<?>> update(
        @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        logger.debug("[update()] updateUserRequestDto : {}", updateUserRequestDto);

        userService.update(updateUserRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                UPDATE_USER_SUCCESS.getMessage()
            )
        );
    }

    @PostMapping("/findid")
    public ResponseEntity<ResponseDto<?>> findId(@RequestBody FindIdRequestDto findIdRequestDto) {
        userService.findId(findIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                FIND_ID_SUCCESS.getMessage()
            )
        );
    }
}
