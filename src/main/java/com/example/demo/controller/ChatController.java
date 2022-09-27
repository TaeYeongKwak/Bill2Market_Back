package com.example.demo.controller;

import com.example.demo.exception.client.ClientNotFoundException;
import com.example.demo.model.response.CommonResult;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.ResponseService;
import com.example.demo.service.chat.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"5. Chat"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ClientRepository clientRepository;
    private final ResponseService responseService;
    private final ChatService chatService;

    @ApiOperation(value = "채팅방 목록 조회", notes = "채팅방 목록을 조회한다.")
    @GetMapping("/client")
    public CommonResult chatList(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return responseService.getSingleResult(chatService.findClientChatList(Integer.parseInt(auth.getName())));
    }

    @ApiOperation(value = "알림", notes = "알림을 조회한다.")
    @GetMapping("/alarm")
    public CommonResult myAlarm(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getListResult(chatService.getChatAlarmList(Integer.parseInt(auth.getName())));
    }

    @ApiOperation(value = "채팅방 정보 조회", notes = "현재 사용자가 보고있는 아이템에 따른 채팅방 정보를 가져온다.")
    @GetMapping("/{item-id}")
    public CommonResult addChat(@PathVariable("item-id") Integer itemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(chatService.findChat(itemId, Integer.parseInt(auth.getName())));
    }
}
