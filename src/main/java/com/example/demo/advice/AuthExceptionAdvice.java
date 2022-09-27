package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.client.CAccessDeniedException;
import com.example.demo.exception.client.CAuthenticationEntryPointException;
import com.example.demo.model.response.CommonResult;
import com.example.demo.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class AuthExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult authenticationEntryPointException(){
        return responseService.getFailResult(ExceptionList.AUTHENTICATION_ENTRYPOINT.getCode(), ExceptionList.AUTHENTICATION_ENTRYPOINT.getMessage());
    }

    @ExceptionHandler(CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult accessDeniedException(){
        return responseService.getFailResult(ExceptionList.ACCESS_DENIED.getCode(), ExceptionList.ACCESS_DENIED.getMessage());
    }

}
