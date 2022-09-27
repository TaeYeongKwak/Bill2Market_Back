package com.example.demo.service.contract;

import com.example.demo.exception.chat.ChatNotFoundException;
import com.example.demo.exception.contract.ContractNotFoundException;
import com.example.demo.model.chat.Chat;
import com.example.demo.model.contract.*;
import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.MessageType;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.ContractRepositoryCustom;
import com.example.demo.repository.RedisChatRepository;
import com.example.demo.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService{

    private final ContractRepository contractRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final ContractRepositoryCustom contractRepositoryCustom;
    private final RedisChatRepository redisChatRepository;

    @Override
    public Contract addContract(ContractChatRequestDTO contractChatRequestDTO) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(contractChatRequestDTO.getStartDate(), dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(contractChatRequestDTO.getEndDate(), dateTimeFormatter);
        LocalDateTime date1 = startDate.atStartOfDay();
        LocalDateTime date2 = endDate.atStartOfDay();
        int betweenDays = (int) Duration.between(date1, date2).toDays();

        return contractRepository.save(Contract.builder()
                .chat(chatRepository.findById(contractChatRequestDTO.getChatId()).orElseThrow(ChatNotFoundException::new))
                .contractStatus(ContractType.REQUEST)
                .price(contractChatRequestDTO.getPrice() * betweenDays) // price는 하루 빌리는 비용이기때문에 price * betweenDays(총 대여기간)을 해준다.
                .deposit(contractChatRequestDTO.getDeposit())
                .billpayStatus(BillpayStatus.NOTUSE)
                .permissionStatus(PermissionStatus.NOTACCEPT)
                .retrieveStatus(RetrieveStatus.RETURNNOTACCEPT)
                .contractDate(null)
                .startDate(startDate)
                .endDate(endDate)
                .reviewWrite(ReviewWrite.NOTWRITE)
                .build());
    }

    @Override
    public Contract getContract(Integer contractId) {
        return contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
    }

    @Override
    public Contract updateBillyPayStatus(Integer contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
        contract.setBillpayStatus(BillpayStatus.RETURNCOMPLETE);
        return contractRepository.save(contract);
    }

    @Override
    public Contract modifyContract(Integer contractId, Integer contractStatus, Integer clientIndex) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);

        Integer lenterIndex = contract.getChat().getLenter().getClientIndex();
        Integer ownerIndex = contract.getChat().getOwner().getClientIndex();

        if (contractStatus == 1) {
            if (clientIndex == lenterIndex) {
                contract.setPermissionStatus(PermissionStatus.values()[contract.getPermissionStatus().ordinal() | 2]);
                if (contract.getPermissionStatus() == PermissionStatus.ALLACCEPT) {
                    contract.setContractDate(LocalDate.now());
                    contract.setContractStatus(ContractType.TRANSACTION);
                }
            } else {
                contract.setPermissionStatus(PermissionStatus.values()[contract.getPermissionStatus().ordinal() | 1]);
                if (contract.getPermissionStatus() == PermissionStatus.ALLACCEPT) {
                    contract.setContractDate(LocalDate.now());
                    contract.setContractStatus(ContractType.TRANSACTION);
                }
            }
        } else if (contractStatus == 3) {
            if (clientIndex == lenterIndex) {
                contract.setRetrieveStatus(RetrieveStatus.values()[contract.getRetrieveStatus().ordinal() | 2]);
                if (contract.getRetrieveStatus() == RetrieveStatus.ALLACCEPTRETURN) {
                    contract.setContractStatus(ContractType.TERMINATION);
                }
            } else {
                contract.setRetrieveStatus(RetrieveStatus.values()[contract.getRetrieveStatus().ordinal() | 1]);
                if (contract.getRetrieveStatus() == RetrieveStatus.ALLACCEPTRETURN) {
                    contract.setContractStatus(ContractType.TERMINATION);
                }
            }
        }
        return contractRepository.save(contract);
    }

    @Override
    public Contract modifyContract(Integer contractId, String endDate) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        contract.setEndDate(LocalDate.parse(endDate, formatter));
        contract.setContractStatus(ContractType.REQUEST);
        contract.setPermissionStatus(PermissionStatus.NOTACCEPT);
        return contractRepository.save(contract);
    }

    @Override
    public Slice<ContractIBorrowedResponseDTO> findBorrowedItemList(Integer clientIndex, Integer page) {

        return contractRepository.findContractsByClientIndex(clientIndex, PageRequest.of(page, 10));
    }

    @Async
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Override
    public void scheduleContractOneDayBeforeExpireDate() {
        List<ContractScheduleDTO> contractOneDayBeforeList = contractRepositoryCustom.findByExpireBeforeContract(LocalDate.now());
        contractOneDayBeforeList.forEach(contractScheduleDTO -> {
                    messageService.message(
                            ChatMessage.getChatMessage(contractScheduleDTO, MessageType.APPROACH_EXPIRE),
                            contractScheduleDTO.getSenderId()
                    );
                }
        );
    }

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Override
    public void scheduleContractExpireDate() {
        List<ContractScheduleDTO> contractExpireList = contractRepositoryCustom.findByExpireContract(LocalDate.now());
        contractExpireList.forEach(contractScheduleDTO -> {
                    contractRepositoryCustom.modifyState(contractScheduleDTO.getContractId(), ContractType.values()[2]);
                    messageService.message(
                            ChatMessage.getChatMessage(contractScheduleDTO, MessageType.EXPIRE),
                            contractScheduleDTO.getSenderId()
                    );
                }
        );
    }

    @Override
    public Contract getContractByChatId(Integer chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        redisChatRepository.setChatMessageUrl(String.valueOf(chat.getChatId()), chat.getFileName());
        return contractRepository.findByChat(chat).orElseGet(() -> Contract.builder()
                .chat(chat)
                .build());
    }

    @Override
    public BillpayStatus modifyBillPayStatus(Integer contractId, Integer clientIndex) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
        if(contract.getBillpayStatus() == BillpayStatus.NOTUSE)
            contract.setBillpayStatus(BillpayStatus.REQUESTBILLYPAY);
        else{
            //구매자
            if(contract.getChat().getLenter().getClientIndex() == clientIndex) {
                if (contract.getBillpayStatus() == BillpayStatus.REQUESTBILLYPAY) {
                    contract.setBillpayStatus(BillpayStatus.PAYLENTER);
                } else if (contract.getBillpayStatus() == BillpayStatus.REGISTEROWNERACCOUNT) {
                    contract.setBillpayStatus(BillpayStatus.PAYSUCCESS);
                }
            }
            //판매자
            else if(contract.getChat().getOwner().getClientIndex() == clientIndex){
                if (contract.getBillpayStatus() == BillpayStatus.REQUESTBILLYPAY) {
                    contract.setBillpayStatus(BillpayStatus.REGISTEROWNERACCOUNT);
                } else if (contract.getBillpayStatus() == BillpayStatus.PAYLENTER) {
                    contract.setBillpayStatus(BillpayStatus.PAYSUCCESS);
                }
            }
        }
        return contractRepository.save(contract).getBillpayStatus();
    }

}