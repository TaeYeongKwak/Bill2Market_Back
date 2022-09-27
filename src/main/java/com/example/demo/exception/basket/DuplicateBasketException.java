package com.example.demo.exception.basket;

public class DuplicateBasketException extends RuntimeException{

    public DuplicateBasketException(){
        super();
    }

    public DuplicateBasketException(String message){
        super(message);
    }

    public DuplicateBasketException(String message, Throwable th){
        super(message, th);
    }

}
