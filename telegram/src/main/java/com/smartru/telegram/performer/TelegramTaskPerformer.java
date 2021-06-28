package com.smartru.telegram.performer;

import com.smartru.common.entity.TelegramTask;
import com.smartru.common.model.Performer;
import com.smartru.common.model.TelegramPerformer;
import com.smartru.prime.TaskPerformer;
import com.smartru.telegram.PrimeNumberCheckTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TelegramTaskPerformer implements TelegramPerformer {

    private final Performer performer;
    private final PrimeNumberCheckTelegramBot bot;

    @Autowired
    public TelegramTaskPerformer(Performer performer, PrimeNumberCheckTelegramBot bot) {
        this.performer = performer;
        this.bot = bot;
    }

    @Override
    public void perform(TelegramTask task) {
        task.setResult(performer.perform(task.getTask()));
        bot.resultReturn(task);
    }
}
