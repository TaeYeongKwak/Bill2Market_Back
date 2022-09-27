package com.example.demo.exception.basket;

public class BasketNotFoundException extends RuntimeException{

    public BasketNotFoundException(){
        super();
    }

    public BasketNotFoundException(String message){
        super(message);
    }

    public BasketNotFoundException(String message, Throwable th){
        super(message, th);
    }

}
