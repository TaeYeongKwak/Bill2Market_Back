package com.example.demo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    CLIENT_NOT_FOUNT(-1000, "해당 사용자가 존재하지 않습니다."),
    PASSWORD_MISMATCH(-1001, "비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_ENTRYPOINT(-1002, "해당 기능을 이용하기 위한 권한이 없습니다."),
    ACCESS_DENIED(-1003, "권한이 부족하여 해당 기능을 이용하실 수 없습니다."),
    INPUT_NULL(-1004, "입력하지 않은 값이 있습니다."),
    EXIST_ID(-1005, "이미 존재하는 아이디입니다."),
    HTTP_FAIL(-1006, "외부 서버와 연결에 실패했습니다."),
    ITEM_NOT_FOUND(-1007, "해당 물품이 존재하지 않습니다."),
    BASKET_NOT_FOUND(-1008, "해당 찜이 존재하지 않습니다."),
    DUPLICATE_BASKET(-1009, "이미 찜이 되어있는 물품입니다."),
    EXIST_NICKNAME(-1010, "현재 존재하는 닉네임입니다."),
    CHAT_NOT_FOUND(-1011, "존재하지 않는 채팅입니다."),
    CHATFILE_CREATE_FAIL(-1012, "채팅 파일 생성에 실패하였습니다."),
    CONTRACT_NOT_FOUND(-1013, "해당 계약이 존재하지 않습니다."),
    OPEN_BANK_TOKEN_ERROR(-1020,"은행 토큰 획득에 실패하였습니다."),
    OPEN_BANK_USERINFO_ERROR(-1021, "은행에서 사용자 정보를 불러오는데 실패하였습니다."),
    OPEN_BANK_TRANSFER_ERROR(-1022, "결제에 실패하였습니다."),
    OPEN_BANK_CSRF_ERROR(-1023, "잘못된 경로로 시도하였습니다. 다시 시도해주세요."),
    DUPLICATE_ITEM_REVIEW(-1031, "이미 해당 물품의 리뷰를 작성하였습니다."),
    CONTRACT_BILLPAY_STATUS_ERROR(-1032, "해당 계약에 대한 빌리페이 상태처리에 문제가 발생하였습니다.");

    private final int code;
    private final String message;
}
