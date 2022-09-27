package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.item.ItemNotFoundException;
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
public class ItemExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult itemNotFoundException(){
        return responseService.getFailResult(ExceptionList.ITEM_NOT_FOUND.getCode(), ExceptionList.ITEM_NOT_FOUND.getMessage());
    }

}
