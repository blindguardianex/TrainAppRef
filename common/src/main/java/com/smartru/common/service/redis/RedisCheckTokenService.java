package com.smartru.common.service.redis;

public interface RedisCheckTokenService {

    boolean tokenExists(String token);
}
