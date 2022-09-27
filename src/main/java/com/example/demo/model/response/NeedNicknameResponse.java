package com.example.demo.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeedNicknameResponse<T> extends CommonResult {
    private T clientIndex;
}
