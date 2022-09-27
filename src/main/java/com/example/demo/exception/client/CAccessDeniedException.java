package com.example.demo.exception.client;

public class CAccessDeniedException extends RuntimeException{

    public CAccessDeniedException(){
        super();
    }

    public CAccessDeniedException(String message){
        super(message);
    }

    public CAccessDeniedException(String message, Throwable th){
        super(message, th);
    }

}
