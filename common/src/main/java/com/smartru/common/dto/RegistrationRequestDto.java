package com.smartru.common.dto;

import com.smartru.common.entity.User;
import lombok.Data;

@Data
public class RegistrationRequestDto {
    private final String username;
    private final String password;

    public User toUser(){
        return new User(username,password);
    }
}
