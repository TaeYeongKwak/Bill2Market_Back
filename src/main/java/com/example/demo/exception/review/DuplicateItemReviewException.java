package com.example.demo.exception.review;

public class DuplicateItemReviewException extends RuntimeException{

    public DuplicateItemReviewException(){
        super();
    }

    public DuplicateItemReviewException(String message){
        super(message);
    }

    public DuplicateItemReviewException(String message, Throwable th){
        super(message, th);
    }

}
