
package com.example.demo.controller;

import com.example.demo.exception.client.ClientNotFoundException;
import com.example.demo.exception.client.ExistIdException;
import com.example.demo.exception.client.ExistNicknameException;
import com.example.demo.exception.client.InputNullException;
import com.example.demo.model.client.Client;
import com.example.demo.model.client.SignupRequest;
import com.example.demo.model.response.CommonResult;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.Client.ClientService;
import com.example.demo.service.ResponseService;
import com.querydsl.core.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = {"2. Client"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ResponseService responseService;
    private final ClientService clientService;

    @ApiOperation(value = "사용자 정보", notes = "사용자 정보를 조회한다.")
    @GetMapping("/me")
    public CommonResult myInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(clientService.findById(Integer.parseInt(auth.getName())));
    }

    @ApiOperation(value = "ID중복 체크", notes = "회원가입 시 ID중복 체크")
    @GetMapping("/id-check")
    public CommonResult checkId(@RequestParam String clientId){
        if(clientRepository.findByClientId(clientId).isPresent()) throw new ExistIdException(); //이미 존재하는 ID exception
        if(StringUtils.isNullOrEmpty(clientId)) throw new InputNullException(); //사용자가 ID 미입력 후 중복확인 버튼 클릭 했을 경우 exception처리
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "닉네임 중복 체크", notes = "회원가입 시 닉네임 중복 체크")
    @GetMapping("/nickname-check")
    public CommonResult checkNickname(@RequestParam String nickname){//이미 존재하는 Nickname있을 때 exception처리
        if(clientRepository.findByNickname(nickname).isPresent()) throw new ExistNicknameException();
        if(StringUtils.isNullOrEmpty(nickname)) throw new InputNullException(); //사용자가 닉네임 미입력 후 중복확인 버튼 클릭 했을 경우 exception처리
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "닉네임 설정", notes = "SNS 회원가입 시 사용자의 닉네임을 설정한다.")
    @PutMapping("/{client-index}/nickname")
    public CommonResult setNickname(@PathVariable("client-index") Integer clientIndex, @RequestParam("nickname")String nickname){
        System.out.println(clientIndex);
        System.out.println(nickname);
        clientService.setNickname(clientIndex, nickname);
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "게시자 리뷰 목록 조회", notes = "게시자의 리뷰들 목록을 가져온다.")
    @GetMapping("/{item-id}/review")
    public CommonResult clientReviewList(@PathVariable("item-id") Integer itemId, @RequestParam Integer page){
        return responseService.getSingleResult(clientService.getReviewByOwnerIndex(itemId, page));
    }

    @ApiOperation(value = "접속한 사용자 물품 리뷰 전체 조회", notes = "현재 접속한 사용자가 올린 물품에 대한 모든 리뷰를 조회한다.")
    @GetMapping("/item-review")
    public CommonResult clientAllItemReviewList(@RequestParam Integer page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(clientService.getItemReviewByOwnerIndex(Integer.parseInt(auth.getName()), page));
    }

    @ApiOperation(value = "사용자 프로필 사진 변경", notes = "현재 접속한 사용자의 프로필 사진을 변경한다.")
    @PutMapping("/client-photo")
    public CommonResult modifyClientProfile(@RequestPart MultipartFile clientPhoto) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        clientService.modifyClientPhoto(Integer.parseInt(auth.getName()), clientPhoto);
        return responseService.getSuccessfulResult();
    }
}