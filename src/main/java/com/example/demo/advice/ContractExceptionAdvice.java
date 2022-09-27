package com.example.demo.advice;


import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.contract.*;
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
public class ContractExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(ContractNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult contractNotFoundException(){
        return responseService.getFailResult(ExceptionList.CONTRACT_NOT_FOUND.getCode(), ExceptionList.CONTRACT_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(ContractBillPayStatusErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult contractBillPayStatusError(){
        return responseService.getFailResult(ExceptionList.CONTRACT_BILLPAY_STATUS_ERROR.getCode(), ExceptionList.CONTRACT_BILLPAY_STATUS_ERROR.getMessage());
    }

    @ExceptionHandler(OpenBankTokenErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult openBankTokenError(){
        return responseService.getFailResult(ExceptionList.OPEN_BANK_TOKEN_ERROR.getCode(), ExceptionList.OPEN_BANK_TOKEN_ERROR.getMessage());
    }

    @ExceptionHandler(OpenBankUserInfoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult openBankUserInfoError(){
        return responseService.getFailResult(ExceptionList.OPEN_BANK_USERINFO_ERROR.getCode(), ExceptionList.OPEN_BANK_USERINFO_ERROR.getMessage());
    }

    @ExceptionHandler(OpenBankTransferErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult openBankTransferError(){
        return responseService.getFailResult(ExceptionList.OPEN_BANK_TRANSFER_ERROR.getCode(), ExceptionList.OPEN_BANK_TRANSFER_ERROR.getMessage());
    }

    @ExceptionHandler(OpenBankCSRFErrorException.class)
    @ResponseStatus
    protected CommonResult openBankCSRFError(){
        return responseService.getFailResult(ExceptionList.OPEN_BANK_CSRF_ERROR.getCode(), ExceptionList.OPEN_BANK_CSRF_ERROR.getMessage());
    }

}
