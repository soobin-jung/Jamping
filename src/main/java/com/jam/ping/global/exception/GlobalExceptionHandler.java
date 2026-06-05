package com.jam.ping.global.exception;

import com.jam.ping.global.response.ApiRes;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ResponseStatusException을 공통 응답 형식으로 변환합니다.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiRes<Void>> handleResponseStatusException(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());

        return ResponseEntity.status(status)
                .body(new ApiRes<Void>().responseMsg(status.value(), exception.getReason()));
    }

    /**
     * @Valid 검증 오류를 공통 응답 형식으로 변환합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRes<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        String message = fieldError == null ? "잘못된 요청입니다." : fieldError.getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(new ApiRes<Void>().responseMsg(HttpStatus.BAD_REQUEST.value(), message));
    }

    /**
     * 제약 조건 위반 예외를 공통 응답 형식으로 변환합니다.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiRes<Void>> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiRes<Void>().responseMsg(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    /**
     * 처리되지 않은 예외를 공통 응답 형식으로 변환합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRes<Void>> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiRes<Void>().responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
    }
}
