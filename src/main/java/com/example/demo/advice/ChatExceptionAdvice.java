package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.chat.ChatFileCreateFailedException;
import com.example.demo.exception.chat.ChatNotFoundException;
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
public class ChatExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult chatNotFoundException(){
        return responseService.getFailResult(ExceptionList.CHAT_NOT_FOUND.getCode(), ExceptionList.CHAT_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(ChatFileCreateFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult chatFileCreateFailedException(){
        return responseService.getFailResult(ExceptionList.CHATFILE_CREATE_FAIL.getCode(), ExceptionList.CHATFILE_CREATE_FAIL.getMessage());
    }

}
