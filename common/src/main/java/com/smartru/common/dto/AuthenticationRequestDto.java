package com.smartru.common.dto;

import lombok.Data;

/**
 * Сущность, включающая данные, необходимые для аутентификации пользователя
 */
@Data
public class AuthenticationRequestDto {
    private final String username;
    private final String password;
}
