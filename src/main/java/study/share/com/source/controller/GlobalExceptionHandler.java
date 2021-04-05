package study.share.com.source.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import study.share.com.source.model.exception.GeneralErrorException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(
            GeneralErrorException.class)
    public ResponseEntity<Object> handleGeneralException(
            Exception ex, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요", headers, HttpStatus.BAD_REQUEST);
    }
}
