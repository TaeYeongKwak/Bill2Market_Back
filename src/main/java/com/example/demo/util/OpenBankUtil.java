package com.example.demo.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

public class OpenBankUtil {

    public static String getRandState(){
        return RandomStringUtils.randomAlphanumeric(32);
    }

    public static String getRandBankTranId(String institutionCode){
        return institutionCode + "U" + RandomStringUtils.randomAlphanumeric(9).toUpperCase();
    }

    public static LocalDateTime getExpireDateTime(String expireAt){
        return LocalDateTime.now().plusSeconds(Long.parseLong(expireAt));
    }

}
