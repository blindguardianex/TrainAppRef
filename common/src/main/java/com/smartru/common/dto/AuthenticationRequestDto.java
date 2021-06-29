package com.smartru.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private final String username;
    private final String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AuthenticationRequestDto(@JsonProperty("username") String username,
                                    @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }
}
