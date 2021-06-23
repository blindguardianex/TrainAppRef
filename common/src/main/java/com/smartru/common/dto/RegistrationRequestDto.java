package com.smartru.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartru.common.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequestDto {

    @NotBlank
    @Size(min = 5, message = "Минимальная длинна логина: 5 символов")
    private final String username;

    @NotBlank
    @Size(min = 6, message = "Минимальная длинна пароля: 6 символов")
    private final String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RegistrationRequestDto(@JsonProperty("username") String username,
                                  @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public User toUser(){
        return new User(username,password);
    }
}
