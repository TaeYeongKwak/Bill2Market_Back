package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.exception.client.*;
import com.example.demo.model.client.Client;
import com.example.demo.model.client.LoginRequest;
import com.example.demo.model.client.Role;
import com.example.demo.model.client.SignupRequest;
import com.example.demo.model.response.CommonResult;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.Client.ClientService;
import com.example.demo.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"1. Auth"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final ResponseService responseService;
    private final ClientService clientService;

    @ApiOperation(value = "로그인", notes = "회원 로그인 기능")
    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginRequest loginRequest){
        Client client = clientRepository.findByClientId(loginRequest.getClientId()).orElseThrow(ClientNotFoundException::new);
        if (!passwordEncoder.matches(loginRequest.getPassword(), client.getPassword())) throw new PasswordMisMatchException();
        return responseService.getSingleResult(jwtTokenProvider.createToken(client.getClientIndex(), client.getRole()));
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 기능")
    @PostMapping("/signup")
    public CommonResult signUp(@Valid @RequestBody SignupRequest signupRequest){
        if(clientRepository.findByClientId(signupRequest.getClientId()).isPresent()) throw new ExistIdException();
        if(clientRepository.findByNickname(signupRequest.getNickname()).isPresent()) throw new ExistNicknameException();
        clientRepository.save(signupRequest.toEntity(passwordEncoder));
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "Naver 로그인", notes = "Naver 로그인 기능")
    @PostMapping("/naver-login")
    public CommonResult naverLogin(@RequestParam("access_token")String accessToken){
        return clientService.getClientFromNaver(accessToken);
    }
}
