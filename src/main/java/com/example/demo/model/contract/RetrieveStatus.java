package com.example.demo.model.contract;

public enum RetrieveStatus {

    RETURNNOTACCEPT, //반납완료 미동의
    OWNERACCEPTRETURN, //판매자 반납완료 동의
    LENTERACCEPTRETURN, //구매자 반납완료 동의
    ALLACCEPTRETURN //판매자, 구매자 반납완료 동의

}
