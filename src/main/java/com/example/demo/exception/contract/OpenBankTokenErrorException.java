package com.example.demo.exception.contract;

public class OpenBankTokenErrorException extends RuntimeException{

    public OpenBankTokenErrorException(){
        super();
    }

    public OpenBankTokenErrorException(String message){
        super(message);
    }

    public OpenBankTokenErrorException(String message, Throwable th){
        super(message, th);
    }
}
