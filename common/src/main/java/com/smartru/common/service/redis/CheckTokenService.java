package com.smartru.common.service.redis;

public interface CheckTokenService {

    boolean tokenExists(String token);

    default boolean isReady(){
        return true;
    }
}
