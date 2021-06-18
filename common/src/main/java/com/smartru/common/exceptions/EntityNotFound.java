package com.smartru.common.exceptions;


/**
 * Непроверяемое исключение, возникающее в случае попытки обновления
 * несуществующей сущности
 * Изменил на использование стандартного исключения:
 * @see javax.persistence.EntityNotFoundException
 */
@Deprecated
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
