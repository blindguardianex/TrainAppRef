package com.smartru.common.exceptions;

/**
 * Непроверяемое исключение, возникающее в случае попытки обновления
 * несуществующей сущности
 */
public class EntityNotFound extends RuntimeException {

    public EntityNotFound() {
        super();
    }

    public EntityNotFound(String message) {
        super(message);
    }

    public EntityNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
