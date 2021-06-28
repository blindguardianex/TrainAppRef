package com.smartru.telegram;

import com.smartru.common.bot.BotStarter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class TelegramBotStarter implements BotStarter {

    @Autowired
    private PrimeNumberCheckTelegramBot telegramBot;

    @Override
    public void start() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            log.info("Telegram bot successfully started!");
        } catch (TelegramApiException e) {
            log.error("Telegram bot not started...");
            e.printStackTrace();
        }
    }
}
