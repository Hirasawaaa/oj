package org.example.oj.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    //concurrenthashmap 多个用户同时登录时，线程安全
    private final Map<String,Long> tokenStore= new ConcurrentHashMap<>();

    //登录成功后生成token，关联userid
    public String createToken(Long userId){
        String token= UUID.randomUUID().toString();
        tokenStore.put(token,userId);
        return token;
    }
    //根据token获取userid
    public Long getUserId(String token){
        return tokenStore.get(token);
    }
    //验证token
    public boolean isValid(String token){
        return token!=null && tokenStore.containsKey(token);
    }


}
