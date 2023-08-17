package com.pi.stepup.global.error;

import static com.pi.stepup.global.error.constant.ExceptionMessage.AUTHORIZATION_FAILED;

import com.pi.stepup.global.dto.ResponseDto;
import com.pi.stepup.global.error.exception.DuplicatedException;
import com.pi.stepup.global.error.exception.ForbiddenException;
import com.pi.stepup.global.error.exception.NotFoundException;
import com.pi.stepup.global.error.exception.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ResponseDto<String>> handleDuplicatedException(
        DuplicatedException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ResponseDto.create(exception.getMessage())
        );
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ResponseDto<String>> handleTokenException(TokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ResponseDto.create(exception.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleNotFoundException(
        NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ResponseDto.create(exception.getMessage())
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto<String>> handleForbiddenException(
        ForbiddenException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ResponseDto.create(AUTHORIZATION_FAILED.getMessage())
        );
    }

}
