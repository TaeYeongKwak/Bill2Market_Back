package com.example.demo.service.contract;

import com.example.demo.model.bank.AccountTokenResponseDTO;
import com.example.demo.model.bank.PaymentResultResponseDTO;
import com.example.demo.model.bank.TransferRequestDTO;
import com.example.demo.model.contract.BillpayStatus;

public interface OpenBankService {
    public String getOpenBankOAuthUrl(Integer clientIndex, Integer contractId);
    public void registerUserInfoToken(String code, Integer clientIndex, String state);
    public BillpayStatus transfer(Integer clientIndex, TransferRequestDTO transferRequestDTO);
    public AccountTokenResponseDTO tokenRequestDTO();
    public void depositLenterTransfer(Integer contractId);
    public void depositOwnerTransfer(Integer contractId);
}
