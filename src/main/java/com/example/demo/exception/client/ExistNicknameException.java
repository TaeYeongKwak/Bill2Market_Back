package com.example.demo.exception.client;

public class ExistNicknameException extends RuntimeException{//이미 존재하는 닉네임 있을 시 예외 처리

    public ExistNicknameException(){
        super();
    }

    public ExistNicknameException(String message){
        super(message);
    }

    public ExistNicknameException(String message, Throwable th){
        super(message, th);
    }
}
