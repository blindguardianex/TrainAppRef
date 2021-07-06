package com.smartru.common.service.token;

public interface CheckTokenService {

    boolean tokenExists(String token);

    default boolean isReady(){
        return true;
    }
}
