package com.example.demo.controller;

import com.example.demo.model.bank.TransferRequestDTO;
import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.MessageType;
import com.example.demo.model.client.Client;
import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ContractChatRequestDTO;
import com.example.demo.model.contract.ContractMessage;
import com.example.demo.model.response.CommonResult;
import com.example.demo.service.Client.ClientService;
import com.example.demo.service.ResponseService;
import com.example.demo.service.chat.MessageService;
import com.example.demo.service.contract.ContractService;
import com.example.demo.service.contract.OpenBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Api(tags = {"6. Contract"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ResponseService responseService;
    private final ContractService contractService;
    private final MessageService messageService;
    private final OpenBankService openBankService;
    private final ClientService clientService;

    @ApiOperation(value = "채팅에 대한 계약 정보 조회", notes = "채팅에 대한 계약 정보를 조회한다.")
    @GetMapping("/{chat-id}")
    public CommonResult getContractInfoByChatId(@PathVariable("chat-id") Integer chatId){
        return responseService.getSingleResult(contractService.getContractByChatId(chatId));
    }

    @ApiOperation(value = "채팅에 대한 계약 작성", notes = "채팅 정보에 맞는 계약을 작성한다.")
    @PostMapping("/{chat-id}")
    public CommonResult contract(@PathVariable("chat-id") Integer chatId, @RequestBody ContractChatRequestDTO contractChatRequestDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        contractChatRequestDTO.setChatId(chatId);
        Contract contract = contractService.addContract(contractChatRequestDTO);
        Client client = clientService.findById(Integer.parseInt(auth.getName()));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderId(client.getClientIndex())
                .senderNickname(client.getNickname())
                .messageType(MessageType.TRANS_REQUEST)
                .message(new ContractMessage(
                        contract.getContractId(),
                        contract.getStartDate(),
                        contract.getEndDate())
                        .toString())
                .build();
        messageService.message(chatMessage, Integer.parseInt(auth.getName()));
        return responseService.getSingleResult(contract);
    }

    @ApiOperation(value = "계약 상태 변경", notes = "계약의 상태를 변경한다.")
    @PutMapping("/status/{contract-id}")
    public CommonResult modifyContractStatus(@PathVariable("contract-id") Integer contractId, @RequestParam Integer contractStatus){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Contract contract = contractService.modifyContract(contractId, contractStatus, Integer.parseInt(auth.getName()));
        Client client = clientService.findById(Integer.parseInt(auth.getName()));
        MessageType messageType = MessageType.TRANS_REQUEST;
        if (contractStatus == 1) messageType = MessageType.TRANS_ACCEPT;
        else if (contractStatus == 3) messageType = MessageType.TRANS_END;
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderId(client.getClientIndex())
                .senderNickname(client.getNickname())
                .messageType(messageType)
                .message(String.valueOf(contract.getContractId()))
                .build();
        messageService.message(chatMessage, contract.getChat().getChatId());
        return responseService.getSingleResult(contract.getContractStatus());
    }

    @ApiOperation(value = "계약 마감일 변경", notes = "계약의 마감일을 변경한다.")
    @PutMapping("/end-date/{contract-id}")
    public CommonResult modifyContractEndDate(@PathVariable("contract-id") Integer contractId, @RequestParam String endDate){
        Contract contract = contractService.modifyContract(contractId, endDate);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientService.findById(Integer.parseInt(auth.getName()));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderId(client.getClientIndex())
                .senderNickname(client.getNickname())
                .messageType(MessageType.TRANS_REQUEST)
                .message(String.valueOf(contract.getContractId()))
                .build();
        messageService.message(chatMessage, contract.getChat().getChatId());
        return responseService.getSingleResult(contract.getEndDate());
    }

    @ApiOperation(value = "빌린 물품 조회", notes = "내가 계약한(빌린) 물품들을 조회한다.")
    @GetMapping("/me")
    public CommonResult myItemBorrowedList(Integer page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(contractService.findBorrowedItemList(Integer.parseInt(auth.getName()), page));
    }

    @ApiOperation(value = "거래 계좌 등록", notes = "계약에 필요한 계좌를 등록한다.")
    @GetMapping("/account/{contract-id}")
    public CommonResult getAccountURL(@PathVariable("contract-id") Integer contractId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uri = openBankService.getOpenBankOAuthUrl(Integer.parseInt(auth.getName()), contractId);
        return uri.equals("0")? responseService.getSuccessfulResult(): responseService.getNeedAccount(uri);
    }

    @ApiIgnore
    @RequestMapping("/account/first/lenter")
    public void accountFirstLenter(@RequestParam String code, @RequestParam("client_info") String clientIndexAndContractId, @RequestParam String state, HttpServletResponse response) throws IOException {
        String[] idArray = clientIndexAndContractId.split("-");
        Integer clientIndex = Integer.parseInt(idArray[0]);
        String contractId = idArray[1];
        openBankService.registerUserInfoToken(code, clientIndex, state);
        response.sendRedirect("https://bill2market.com/loading?contractId=" + contractId);
    }

    @ApiIgnore
    @RequestMapping("/account/first/owner")
    public void accountFirstOwner(@RequestParam String code, @RequestParam("client_info") String clientIndexAndContractId, @RequestParam String state, HttpServletResponse response) throws IOException {
        String[] idArray = clientIndexAndContractId.split("-");
        Integer clientIndex = Integer.parseInt(idArray[0]);
        openBankService.registerUserInfoToken(code, clientIndex, state);
        response.sendRedirect("https://bill2market.com/blank");
    }

    @PostMapping("/transfer")
    public CommonResult transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(openBankService.transfer(Integer.parseInt(auth.getName()), transferRequestDTO));
    }

    @ApiOperation(value = "보증금 돌려주기 구현", notes = "빌리페이 계좌에서 사용자 계좌에 이체된 보증금을 돌려준다.")
    @PostMapping("/deposit")
    public CommonResult contractDeposit(@RequestParam("contract-id")Integer contractId){
        openBankService.tokenRequestDTO();
        openBankService.depositLenterTransfer(contractId);
        openBankService.depositOwnerTransfer(contractId);
        contractService.updateBillyPayStatus(contractId);
        Contract contract = contractService.getContract(contractId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientService.findById(Integer.parseInt(auth.getName()));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderId(client.getClientIndex())
                .senderNickname(client.getNickname())
                .messageType(MessageType.BILL_END)
                .message(String.valueOf(contract.getContractId()))
                .build();
        messageService.message(chatMessage, contract.getChat().getChatId());
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "빌리페이 상태 변경", notes = "해당 계약에 대한 빌리페이 상태를 변경한다.")
    @PutMapping("/bill-pay/status/{contract-id}")
    public CommonResult contractDepositStatus(@PathVariable("contract-id") Integer contractId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(contractService.modifyBillPayStatus(contractId, Integer.parseInt(auth.getName())));
    }
}
