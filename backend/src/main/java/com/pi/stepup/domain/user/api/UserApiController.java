package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_ID_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_PASSWORD_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.DELETE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.FIND_ID_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.FIND_PASSWORD_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.LOGIN_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ONE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.REISSUE_TOKENS_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.SIGN_UP_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.UPDATE_USER_SUCCESS;

import com.pi.stepup.domain.user.dto.UserRequestDto.AuthenticationRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.FindIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.FindPasswordRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.ReissueTokensRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.SignUpRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.UpdateUserRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.service.UserService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user", description = "user domain apis")
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @Operation(summary = "국가 코드 목록 조회", description = "선택 가능한 국가 코드 목록을 조회한다.")
    @ApiResponse(responseCode = "200",
        description = "국가 코드 목록 조회 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = CountryResponseDto.class))))
    @GetMapping("/country")
    public ResponseEntity<ResponseDto<?>> readAllCountries() {
        return ResponseEntity.ok(
            ResponseDto.create(
                READ_ALL_COUNTRIES_SUCCESS.getMessage(),
                userService.readAllCountries()
            )
        );
    }

    @Operation(summary = "이메일 중복 검사", description = "중복된 이메일 데이터가 있는지 검사한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "이메일 사용 가능")
    })
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
        log.debug("[update()] updateUserRequestDto : {}", updateUserRequestDto);

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

    @PostMapping("/findpw")
    public ResponseEntity<ResponseDto<?>> findPassword(
        @RequestBody FindPasswordRequestDto findPasswordRequestDto) {
        userService.findPassword(findPasswordRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(
                FIND_PASSWORD_SUCCESS.getMessage()
            )
        );
    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseDto<?>> reissueTokens(
        @RequestHeader("refreshToken") String refreshToken,
        @RequestBody ReissueTokensRequestDto reissueTokensRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDto.create(
                REISSUE_TOKENS_SUCCESS.getMessage(),
                userService.reissueTokens(refreshToken, reissueTokensRequestDto)
            )
        );
    }
}
