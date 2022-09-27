package com.example.demo.repository;

import com.example.demo.model.chat.Chat;
import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ContractIBorrowedResponseDTO;
import com.example.demo.model.contract.DepositForClientDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query(name = "ContractIBorrowedByClientIndex", nativeQuery = true)
    public Slice<ContractIBorrowedResponseDTO> findContractsByClientIndex(@Param("client_index") Integer clientIndex, Pageable page);

    @Query(name = "findClientInfo", nativeQuery = true) //결제를 위한 clientInfo에 관련된 쿼리
    public Optional<DepositForClientDTO> findLenterByContractId(@Param("contract_id") Integer contractId);

    public Optional<Contract> findByChat(Chat chat);
}
