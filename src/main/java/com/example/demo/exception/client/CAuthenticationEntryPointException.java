package com.example.demo.exception.client;

public class CAuthenticationEntryPointException extends RuntimeException{

    public CAuthenticationEntryPointException(){
        super();
    }

    public CAuthenticationEntryPointException(String message){
        super(message);
    }

    public CAuthenticationEntryPointException(String message, Throwable th){
        super(message, th);
    }

}
