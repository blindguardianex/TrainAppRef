package com.smartru.common.service.redis;

public interface TokenService extends CheckTokenService{

    void addToken(String userId, String token);
}
