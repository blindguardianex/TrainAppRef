package com.smartru.telegram.commands;

import com.smartru.common.entity.Task;
import com.smartru.performers.factorial.FactorialPerformer;
import com.smartru.telegram.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class FactorialCommand implements IBotCommand {

    private final String IDENTIFIER = "F";
    private final String DESCRIPTION = "Факториал";
    private final FactorialPerformer performer;

    public FactorialCommand(FactorialPerformer performer) {
        this.performer = performer;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        String num = strings[0];
        String result = performer.perform(new Task(num));

        SendMessage msg = Util.createMessage(message, String.format("%s! = %s", num, result));
        try {
            absSender.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    private String usernameFromMessage(Message msg){
        org.telegram.telegrambots.meta.api.objects.User user = msg.getFrom();
        String username = user.getUserName();
        return "@"+username;
    }
}
