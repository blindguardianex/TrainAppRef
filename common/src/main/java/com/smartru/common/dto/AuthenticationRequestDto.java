package com.smartru.common.dto;

import lombok.Data;

/**
 * Сущность, включающая данные, необходимые для аутентификации пользователя
 */
@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
