package com.example.demo.repository;

import com.example.demo.model.bank.OpenBankClientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class RedisOpenBankRepository {

    private static final String CLIENT_INFO = "CLIENT_INFO";

    private HashOperations<String, String, OpenBankClientInfo> hashOpsClientInfo;

    public RedisOpenBankRepository(RedisTemplate<String, Object> redisTemplateHash){
        this.hashOpsClientInfo = redisTemplateHash.opsForHash();
    }

    public void addClientInfo(Integer clientIndex, OpenBankClientInfo openBankClientInfo){
        this.hashOpsClientInfo.put(CLIENT_INFO, String.valueOf(clientIndex), openBankClientInfo);
    }

    public Optional<OpenBankClientInfo> getClientInfo(Integer clientIndex){
        return Optional.of(this.hashOpsClientInfo.get(CLIENT_INFO, String.valueOf(clientIndex)));
    }

}
