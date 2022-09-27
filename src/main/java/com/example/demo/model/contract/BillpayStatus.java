package com.example.demo.model.contract;

public enum BillpayStatus {
    NOTUSE, //사용안함
    REQUESTBILLYPAY, //빌리페이 요청
    REGISTEROWNERACCOUNT, //판매자 계좌 등록
    PAYLENTER, //구매자 결제
    PAYSUCCESS, // 판매자, 구매자 계좌 등록 및 결제 완료
    RETURNCOMPLETE // 반납 완료 및 보증금, 대여료 반환
}
