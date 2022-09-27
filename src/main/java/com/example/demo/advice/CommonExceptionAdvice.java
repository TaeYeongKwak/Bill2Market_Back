package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.client.*;
import com.example.demo.exception.common.HttpFailException;
import com.example.demo.model.response.CommonResult;
import com.example.demo.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult unknown(Exception e){
        log.error("unknown exception", e);
        return responseService.getFailResult(ExceptionList.UNKNOWN.getCode(), ExceptionList.UNKNOWN.getMessage());
    }

    @ExceptionHandler(InputNullException.class) //빈 값 입력 예외 사항 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult InputNullException(){
        return responseService.getFailResult(ExceptionList.INPUT_NULL.getCode(), ExceptionList.INPUT_NULL.getMessage());
    }

    @ExceptionHandler(HttpFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult HttpFailException(){
        return responseService.getFailResult(ExceptionList.HTTP_FAIL.getCode(), ExceptionList.EXIST_ID.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult blankDataError(MethodArgumentNotValidException e){
        return responseService.getFailResult(-1030, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
