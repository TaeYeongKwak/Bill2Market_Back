package com.example.demo.repository;

import com.example.demo.model.bank.BillyPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillyPayRepository extends JpaRepository<BillyPay, Integer> {
    public boolean existsByClientIndex(@Param("client_index") Integer clientIndex);
    public Optional<BillyPay> findByClientIndex(@Param("client_index") Integer clientIndex);
}
