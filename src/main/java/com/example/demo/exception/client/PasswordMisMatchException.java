package com.example.demo.exception.client;

public class PasswordMisMatchException extends RuntimeException{

    public PasswordMisMatchException(){
        super();
    }

    public PasswordMisMatchException(String message){
        super(message);
    }

    public PasswordMisMatchException(String message, Throwable th){
        super(message, th);
    }

}
