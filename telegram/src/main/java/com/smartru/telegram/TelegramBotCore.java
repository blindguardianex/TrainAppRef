package com.smartru.telegram;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.telegram.commands.DecomposeNumberCommand;
import com.smartru.telegram.commands.FactorialCommand;
import com.smartru.telegram.commands.geometric.GeometrySolveCommand;
import com.smartru.telegram.commands.HelpCommand;
import com.smartru.telegram.commands.NonCommandProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

@Slf4j
@Component
public class TelegramBotCore extends TelegramLongPollingCommandBot {

    @Value("${bot.telegram.name}")
    private String BOT_NAME;
    @Value("${bot.telegram.token}")
    private String BOT_TOKEN;

    private final GeometrySolveCommand geometrySolveCommand;
    private final NonCommandProcess nonCommand;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Date START_DATE;


    @Autowired
    public TelegramBotCore(HelpCommand helpCommand,
                           NonCommandProcess nonCommand,
                           DecomposeNumberCommand decomposeNumberCommand,
                           FactorialCommand factorialCommand,
                           GeometrySolveCommand geometrySolveCommand){
        this.nonCommand = nonCommand;
        this.geometrySolveCommand = geometrySolveCommand;
        START_DATE = new Date();
        register(helpCommand);
        register(decomposeNumberCommand);
        register(factorialCommand);
        register(geometrySolveCommand);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message msg = messageFromUpdate(update);
            if (outOfDate(msg)) {
                return;
            } else {
                nonCommand.nonCommandExecute(this, msg);
            }
        }
        else if (update.hasCallbackQuery()){
            if(isCallbackFromGeometricProblem(update)){
                geometrySolveCommand.processCallBack(this, update);
            }
        }
    }

    public void sendAnswer(String result, String chatId){
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.setText(result);
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    private boolean outOfDate(Message message){
        return START_DATE.getTime()/1000000>message.getDate()/1000;
    }

    private Message messageFromUpdate(Update update){
        if(update.getEditedMessage()!=null){
            return update.getEditedMessage();
        } else {
            return update.getMessage();
        }
    }

    private boolean isCallbackFromGeometricProblem(Update callback){
        return callback.getCallbackQuery().getData().startsWith("/solve");
    }


}
