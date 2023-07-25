package com.pi.stepup.global.error;

import com.pi.stepup.global.dto.ResponseDto;
import com.pi.stepup.global.error.exception.DuplicatedException;
import com.pi.stepup.global.error.exception.NotFoundException;
import javax.naming.AuthenticationException;
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDto<String>> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ResponseDto.create(exception.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ResponseDto.create(exception.getMessage())
        );
    }
}
