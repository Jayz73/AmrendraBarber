package com.climbingfox.barber.controller;

import com.climbingfox.barber.dto.APIException;
import com.climbingfox.barber.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = APIException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAPIException(APIException e) {
        return Mono.just(ResponseEntity.badRequest().body(e.getErrorResponse()));
    }
}
