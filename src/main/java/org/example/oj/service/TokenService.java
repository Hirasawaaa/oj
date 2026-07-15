package org.example.oj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    //concurrenthashmap 多个用户同时登录时，线程安全
//    private final Map<String,Long> tokenStore= new ConcurrentHashMap<>();
//
//    //登录成功后生成token，关联userid
//    public String createToken(Long userId){
//        String token= UUID.randomUUID().toString();
//        tokenStore.put(token,userId);
//        return token;
//    }
//    //根据token获取userid
//    public Long getUserId(String token){
//        return tokenStore.get(token);
//    }
//    //验证token
//    public boolean isValid(String token){
//        return token!=null && tokenStore.containsKey(token);
//    }
    @Autowired
    private StringRedisTemplate redisTemplate;

    //Token过期时间 7天
    private static final long EXPIRE_SECONDS=7*24*60*60;

    public String createToken(Long userId){
        String token=UUID.randomUUID().toString();
        String key="token:"+token;
        redisTemplate.opsForValue().set(key,String.valueOf(userId),EXPIRE_SECONDS, TimeUnit.SECONDS);
        return token;
    }
    public Long getUserId(String token){
        String key="token:"+token;
        String value=redisTemplate.opsForValue().get(key);
        return value!=null ? Long.parseLong(value):null;
    }
    public boolean isValid(String token){
        if(token==null || token.isBlank())return false;
        String key="token:"+token;
        return redisTemplate.hasKey(key);
    }
}
