package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.basket.BasketNotFoundException;
import com.example.demo.exception.basket.DuplicateBasketException;
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
public class BasketExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(DuplicateBasketException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult duplicateBasketException(){
        return responseService.getFailResult(ExceptionList.DUPLICATE_BASKET.getCode(), ExceptionList.DUPLICATE_BASKET.getMessage());
    }

    @ExceptionHandler(BasketNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult basketNotFoundException(){
        return responseService.getFailResult(ExceptionList.BASKET_NOT_FOUND.getCode(), ExceptionList.BASKET_NOT_FOUND.getMessage());
    }

}
