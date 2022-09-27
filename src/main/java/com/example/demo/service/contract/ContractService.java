package com.example.demo.service.contract;

import com.example.demo.model.contract.BillpayStatus;
import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ContractChatRequestDTO;
import com.example.demo.model.contract.ContractIBorrowedResponseDTO;
import org.springframework.data.domain.Slice;

public interface ContractService {

    public Contract addContract(ContractChatRequestDTO contractChatRequestDTO);
    public Contract getContract(Integer contractId);
    public Contract updateBillyPayStatus(Integer contractId);
    public Contract modifyContract(Integer contractId, Integer contractStatus, Integer clientIndex);
    public Contract modifyContract(Integer contractId, String endDate);
    public Slice<ContractIBorrowedResponseDTO> findBorrowedItemList(Integer clientIndex, Integer page);
    public void scheduleContractOneDayBeforeExpireDate();
    public void scheduleContractExpireDate();
    public Contract getContractByChatId(Integer chatId);
    public BillpayStatus modifyBillPayStatus(Integer contractId, Integer clientIndex);
}
