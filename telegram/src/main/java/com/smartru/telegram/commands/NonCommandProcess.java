package com.smartru.telegram.commands;

import com.smartru.common.model.Calculator;
import com.smartru.performers.calculator.math.ExpressionChecker;
import com.smartru.telegram.Util;
import com.smartru.telegram.commands.geometric.GeometrySolveCommand;
import com.smartru.telegram.model.PersonalRequestProcess;
import com.smartru.telegram.model.UserProblemContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * TODO: отрефакторить это говно
 */
@Component
public class NonCommandProcess {

    @Autowired
    @Qualifier("mathCalculator")
    private Calculator calculator;
    @Autowired
    private DecomposeNumberCommand decomposeNumberCommand;
    @Autowired
    private PersonalRequestProcess personalRequest;
    @Autowired
    private GeometrySolveCommand geometrySolveCommand;
    private final String NUMBER_PATTERN = "\\A\\d*\\Z";
    private final String BOT_NAME = "@PRIMENUMBERCHECKERBOT";
    private final String BOT_HUMANITY_NAME = "ПИФАГОР";
    private final DecimalFormat formatter = new DecimalFormat("#0.000");

    public void nonCommandExecute(AbsSender sender, Message message){
        if(UserProblemContext.contains(message.getFrom().getId()) &&
                !UserProblemContext.get(message.getFrom().getId()).isEquipped()){
            if (isNumber(message.getText())) {
                geometrySolveCommand.setParameterInProblemCache(message);
                return;
            }
        }
        else if (isPersonalExecute(message)){
            performPersonalRequest(sender, message);
        }
        else if (isNumber(message.getText())){
            checkNumber(sender, message);
        }
        else if (ExpressionChecker.isExpression(message.getText())){
            String result = performExpression(message);
            SendMessage msg = Util.createMessage(message, result);
            Util.sendToTelegram(sender,msg);
        }
    }

    private boolean isPersonalExecute(Message message){
        return message.getText().toUpperCase().startsWith(BOT_NAME) ||
                message.getText().toUpperCase().startsWith(BOT_HUMANITY_NAME);
    }

    private boolean isNumber(String text){
        return text.matches(NUMBER_PATTERN);
    }

    private void performPersonalRequest(AbsSender sender, Message message){
        personalRequest.execute(sender,message);
    }

    private void checkNumber(AbsSender sender, Message msg){
        decomposeNumberCommand.processMessage(sender,
                msg,
                new String[]{msg.getText()});
    }

    private String performExpression(Message message){
        try {
            double result = calculator.solve(message.getText());
            String formattedResult = formatResult(result);
            String answer = message.getText()+" = "+formattedResult;
            return answer;
        } catch (ParseException e) {
            return e.getMessage();
        }
    }

    private String formatResult(double result){
        String formatted = formatter.format(result);
        if (formatted.endsWith(".000")){
            formatted = formatted.substring(0,formatted.length()-4);
        }
        return formatted;
    }
}
