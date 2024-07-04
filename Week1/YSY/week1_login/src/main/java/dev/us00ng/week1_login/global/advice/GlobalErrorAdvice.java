package dev.us00ng.week1_login.global.advice;

import dev.us00ng.week1_login.global.common.BaseException;
import dev.us00ng.week1_login.global.common.BaseResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse> handleException(BaseException e) {
        return ResponseEntity.ok(new BaseResponse(e.getStatus()));
    }
}
