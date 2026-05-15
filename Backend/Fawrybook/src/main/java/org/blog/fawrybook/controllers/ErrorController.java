package org.blog.fawrybook.controllers;

import lombok.extern.slf4j.Slf4j;
import org.blog.fawrybook.domain.dtos.ApiErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDto> handleException(Exception ex){
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred").
                build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex){
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponseDto> handleIllegalStateException(IllegalStateException ex){
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleBadCredentialsException(BadCredentialsException ex){
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }
}
