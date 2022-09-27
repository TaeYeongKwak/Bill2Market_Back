package com.example.demo.exception.contract;

public class OpenBankUserInfoException extends RuntimeException{

    public OpenBankUserInfoException(){
        super();
    }

    public OpenBankUserInfoException(String message){
        super(message);
    }

    public OpenBankUserInfoException(String message, Throwable th){
        super(message, th);
    }

}
