package com.smartru.telegram.performer;

import com.smartru.common.entity.Task;
import com.smartru.common.model.Performer;
import com.smartru.common.model.VoidPerformer;
import com.smartru.telegram.PrimeNumberCheckTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("telegramPrimeNumberCheckerPerformer")
public class TelegramTaskPerformer implements VoidPerformer {

    private final Performer performer;
    private final PrimeNumberCheckTelegramBot bot;

    @Autowired
    public TelegramTaskPerformer(@Qualifier("PrimeNumberCheckerPerformer") Performer performer,
                                 PrimeNumberCheckTelegramBot bot) {
        this.performer = performer;
        this.bot = bot;
    }

    @Override
    public void perform(Task task) {
        task.setResult(performer.perform(task));
        bot.resultReturn(task);
    }
}
