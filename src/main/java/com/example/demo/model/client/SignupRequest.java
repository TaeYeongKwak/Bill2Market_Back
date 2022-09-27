package com.example.demo.model.client;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
public class SignupRequest {
    @NotBlank(message = "ID가 입력되지 않았습니다.")
    private String clientId;
    @NotBlank(message = "이름이 입력되지 않았습니다.")
    private String clientName;
    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    private String password;
    @NotBlank(message = "닉네임이 입력되지 않았습니다.")
    private String nickname;
    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    private String email;
    private Date birthdate;
    @NotBlank(message = "전화번호가 입력되지 않았습니다.")
    private String phoneNumber;
    private Role role;
    private SnsType snsType;
    private SubscribeStatus subscribeStatus;

    public Client toEntity(PasswordEncoder passwordEncoder) {
        Client client = new Client();
        client.setClientId(clientId);
        client.setPassword(passwordEncoder.encode(password));
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setClientName(clientName);
        client.setNickname(nickname);
        client.setBirthdate(birthdate);
        client.setRole(role);
        client.setSnsType(SnsType.SIGNUP);
        client.setSubscribeStatus(SubscribeStatus.NOTSUBSCRIBED);

        return client;
    }
}
