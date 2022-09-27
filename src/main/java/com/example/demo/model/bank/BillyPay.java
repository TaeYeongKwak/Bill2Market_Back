package com.example.demo.model.bank;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Billy_Pay")
public class BillyPay {

    @Id
    @Column(name = "pay_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payId;
    @Column(name = "client_index")
    private Integer clientIndex;
    @Column(name = "fintech_id")
    private String fintechId;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "expire_at")
    private LocalDateTime expireAt;

}
