package com.example.demo.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse<T> extends CommonResult {
    private T token;
    private T clientIndex;
}
