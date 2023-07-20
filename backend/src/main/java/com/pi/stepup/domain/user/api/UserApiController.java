package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;

import com.pi.stepup.domain.user.service.UserService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/country")
    public ResponseEntity<?> readAllCountries() {
        return ResponseEntity.ok(
            ResponseDto.builder()
                .message(READ_ALL_COUNTRIES_SUCCESS.getMessage())
                .data(userService.readAllCountries())
                .build()
        );
    }
}
