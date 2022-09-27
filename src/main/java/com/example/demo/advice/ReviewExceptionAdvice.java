package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.review.DuplicateItemReviewException;
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
public class ReviewExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(DuplicateItemReviewException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult duplicateItemReview(){
        return responseService.getFailResult(ExceptionList.DUPLICATE_ITEM_REVIEW.getCode(), ExceptionList.DUPLICATE_ITEM_REVIEW.getMessage());
    }

}
