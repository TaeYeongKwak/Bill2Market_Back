package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.client.ClientNotFoundException;
import com.example.demo.exception.client.ExistIdException;
import com.example.demo.exception.client.ExistNicknameException;
import com.example.demo.exception.client.PasswordMisMatchException;
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
public class ClientExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult clientNotFoundException(){
        return responseService.getFailResult(ExceptionList.CLIENT_NOT_FOUNT.getCode(), ExceptionList.CLIENT_NOT_FOUNT.getMessage());
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult passwordMisMatchException(){
        return responseService.getFailResult(ExceptionList.PASSWORD_MISMATCH.getCode(), ExceptionList.PASSWORD_MISMATCH.getMessage());
    }

    @ExceptionHandler(ExistIdException.class) //이미 존재하는 아이디 예외 사항 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ExistIdException(){
        return responseService.getFailResult(ExceptionList.EXIST_ID.getCode(), ExceptionList.EXIST_ID.getMessage());
    }

    @ExceptionHandler(ExistNicknameException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ExistNicknameException(){//이미 존재하는 닉네임 예외 사항 처리
        return responseService.getFailResult(ExceptionList.EXIST_NICKNAME.getCode(), ExceptionList.EXIST_NICKNAME.getMessage());
    }
}
