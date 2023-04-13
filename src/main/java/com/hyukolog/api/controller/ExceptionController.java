package com.hyukolog.api.controller;

import com.hyukolog.api.exception.HyukoLogException;
import com.hyukolog.api.exception.InvalidRequest;
import com.hyukolog.api.exception.PostNotFound;
import com.hyukolog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        //MethodArgumentNotValidException
        ErrorResponse errorResponse = ErrorResponse.builder()
                                    .code("400")
                                    .message("잘못된 요청입니다.")
                                    .build();

        for(FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponse;

    }


    @ExceptionHandler(HyukoLogException.class)
    public ResponseEntity<ErrorResponse> hyukoLogExceptionHandler(HyukoLogException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        // 응답 json validation -> title : 제목에 바보를 포함할 수 없습니다.


        return ResponseEntity.status(statusCode)
                .body(body);
    }



}
