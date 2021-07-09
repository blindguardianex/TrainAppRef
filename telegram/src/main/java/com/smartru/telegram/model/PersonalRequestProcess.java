package com.smartru.telegram.model;

import com.smartru.telegram.Util;
import com.smartru.telegram.commands.geometric.GeometrySolveCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;

@Slf4j
@Component
public class PersonalRequestProcess {

    @Autowired
    private GeometrySolveCommand geometrySolve;
    private final String BOT_NAME = "@PRIMENUMBERCHECKERBOT";
    private final String BOT_HUMANITY_NAME = "ПИФАГОР";

    public void execute(AbsSender sender, Message message){
        String request = textFromMessage(message);
        Command requestCommand = Command.getByName(request);
        if (requestCommand.equals(Command.UNKNOWN_COMMAND)){
            String result = "Ничего не понимаю.";
            SendMessage msg = Util.createMessage(message, result);
            Util.sendToTelegram(sender,msg);
        }
        else if (requestCommand.equals(Command.GEOMETRY_PROBLEM)){
            geometrySolve.processMessage(sender, message, new String[0]);
        }
    }

    private String textFromMessage(Message message){
        return message.getText()
                .toUpperCase()
                .replace(BOT_NAME,"")
                .replace(BOT_HUMANITY_NAME,"")
                .replace(", ","");
    }

    private enum Command {
        GEOMETRY_PROBLEM("реши задачку"),
        UNKNOWN_COMMAND("неизвестная задача");

        private final String name;

        Command(String name) {
            this.name = name;
        }

        private static Command getByName(String name){
            return Arrays.stream(Command.values())
                    .filter(command -> command.name.equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(UNKNOWN_COMMAND);
        }
    }
}
