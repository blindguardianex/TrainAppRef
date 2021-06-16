package com.smartru.common.exceptions;

/**
 * Проверяемое исключение, возникающее при попытке сохранения сущности,
 * уникальное поле которой дублирует значение уже сохраненной сущности
 */
public class EntityAlreadyExists extends Exception {

    public EntityAlreadyExists() {
        super();
    }

    public EntityAlreadyExists(String message) {
        super(message);
    }

    public EntityAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
