package com.example.demo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Slf4j
@Repository
public class RedisChatRepository {

    public static final String USER_COUNT = "USER_COUNT";
    public static final String LAST_USER = "LAST_USER";
    public static final String NON_READ_COUNT = "NON_READ_COUNT";
    public static final String CHAT_MESSAGE_URL = "CHAT_MESSAGE_URL";

    private ValueOperations<String, String> valueOps;
    private HashOperations<String, String, String> hashOpsUserInfo;

    public RedisChatRepository(RedisTemplate<String, String> redisTemplateValue, RedisTemplate<String, Object> redisTemplateHash){
        this.valueOps = redisTemplateValue.opsForValue();
        this.hashOpsUserInfo = redisTemplateHash.opsForHash();
    }

    public String getLastUser(String chatId){
        return Optional.ofNullable(valueOps.get(LAST_USER + "_" + chatId)).orElse("-1000");
    }

    public long getUserCount(String chatId) {
        return Long.valueOf(hashOpsUserInfo.size(USER_COUNT + "_" + chatId));
    }

    public void addUser(String chatId, String clientIndex, String sessionId) {
        valueOps.set(LAST_USER + "_" + chatId, clientIndex);
        hashOpsUserInfo.put(USER_COUNT + "_" + chatId, clientIndex, sessionId);
    }

    public void deleteUser(String chatId, String clientIndex) {
        try{
            hashOpsUserInfo.delete(USER_COUNT + "_" + chatId, clientIndex);
            long userCount = getUserCount(chatId);
            if(userCount == 0){ // 혼자 있었을 경우
                valueOps.set(LAST_USER + "_" + chatId, clientIndex);
            }else if(userCount == 1){ // 둘이 있었을 경우
                System.out.println(hashOpsUserInfo.values(USER_COUNT + "_" + chatId).get(0));
                valueOps.set(LAST_USER + "_" + chatId, (String)hashOpsUserInfo.keys(USER_COUNT + "_" + chatId).stream().toArray()[0]);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public int getNonReadCount(String chatId){
        return Integer.valueOf(Optional.ofNullable(valueOps.get(NON_READ_COUNT + "_" + chatId)).orElse("0"));
    }

    public long plusNonReadCount(String chatId){
        return Optional.ofNullable(valueOps.increment(NON_READ_COUNT + "_" + chatId)).orElse(0L);
    }

    public void resetNonReadCount(String chatId){
        valueOps.set(NON_READ_COUNT + "_" + chatId, "0");
    }

    public void setChatMessageUrl(String chatId, String url){
        valueOps.set(CHAT_MESSAGE_URL + "_" + chatId, url);
    }

    public String getChatMessageUrl(String chatId){
        return Optional.ofNullable(valueOps.get(CHAT_MESSAGE_URL + "_" + chatId)).orElse("");
    }
}
