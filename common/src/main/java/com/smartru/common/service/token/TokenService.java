package com.smartru.common.service.token;

public interface TokenService extends CheckTokenService{

    void addToken(String userId, String token);
}
