package com.example.demo.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class jwtConfig {

    @Bean
    public JwtTokenProvider jwtProvider() {
        return new JwtTokenProvider();
    }

}
