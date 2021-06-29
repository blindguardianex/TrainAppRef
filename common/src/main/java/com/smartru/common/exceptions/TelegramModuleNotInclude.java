package com.smartru.common.exceptions;

public class TelegramModuleNotInclude extends RuntimeException{

    public TelegramModuleNotInclude() {
    }

    public TelegramModuleNotInclude(String message) {
        super(message);
    }

    public TelegramModuleNotInclude(String message, Throwable cause) {
        super(message, cause);
    }
}
