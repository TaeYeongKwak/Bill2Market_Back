package com.example.demo.exception.common;

public class HttpFailException extends RuntimeException{
    public HttpFailException(){super();}

    public HttpFailException(String message){
            super(message);
        }

    public HttpFailException(String message, Throwable th){
            super(message, th);
        }

}
