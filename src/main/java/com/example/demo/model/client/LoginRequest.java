package com.example.demo.model.client;

import lombok.Data;

@Data
public class LoginRequest {
    private String clientId;
    private String password;
}
