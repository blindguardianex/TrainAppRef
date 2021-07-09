package com.smartru.telegram.performer;

import com.smartru.common.entity.Task;
import com.smartru.common.model.StringPerformer;
import com.smartru.common.model.VoidPerformer;
import com.smartru.telegram.TelegramBotCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("telegramDecomposeNumberPerformer")
public class TelegramTaskPerformer implements VoidPerformer {

    private final StringPerformer performer;
    private final TelegramBotCore bot;

    @Autowired
    public TelegramTaskPerformer(@Qualifier("decomposeNumberPerformer") StringPerformer performer,
                                 TelegramBotCore bot) {
        this.performer = performer;
        this.bot = bot;
    }

    @Override
    public void perform(Task task) {
        String result = performer.perform(task);
        bot.sendAnswer(result, getChatIdFromTask(task));
    }

    private String getChatIdFromTask(Task task){
        return task.getProperties().get("chatId").asText();
    }
}
