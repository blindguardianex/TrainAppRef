package com.smartru.redis.service.impl;

import com.smartru.redis.service.RedisTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Slf4j
@Service
@Qualifier("redisTokenServiceJedisImpl")
public class RedisTokenServiceJedisImpl implements RedisTokenService {

    @Value("${jedis.key-valid.time}")
    private int keyValidTime;
    private final JedisPool jedisPool;

    @Autowired
    public RedisTokenServiceJedisImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    @Override
    public void addToken(String userId, String token){
        try (Jedis jedis = jedisPool.getResource()){
            jedis.setex(userId, keyValidTime, token);
            jedis.setex(token, keyValidTime, userId);
        } catch (JedisConnectionException ex){
            throw new JedisConnectionException("Redis is not connected");
        }
    }

    @Override
    public boolean tokenExists(String token) {
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.exists(token);
        } catch (JedisConnectionException ex){
            throw new JedisConnectionException("Redis is not connected");
        }
    }
}
