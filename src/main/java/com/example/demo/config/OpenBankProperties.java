package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openbank")
public class OpenBankProperties {

    private String clientId;
    private String clientSecret;
    private String baseUrl;
    private String institutionCode;
    private String name;
    private String bankCode;
    private String accountNum;
    private String fintechId;

}
