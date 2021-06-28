package com.smartru.common.model;

import com.smartru.common.entity.TelegramTask;

public interface TelegramPerformer {

    void perform(TelegramTask task);
}
