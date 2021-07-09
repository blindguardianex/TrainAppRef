package com.smartru.telegram.commands.geometric;

import com.smartru.common.model.Calculator;
import com.smartru.performers.geometric.shape.Desired;
import com.smartru.performers.geometric.shape.Shape;
import com.smartru.telegram.Util;
import com.smartru.telegram.commands.geometric.keyboards.fabric.CircleKeyboardFabric;
import com.smartru.telegram.commands.geometric.keyboards.SelectShapeKeyboard;
import com.smartru.telegram.commands.geometric.keyboards.fabric.RectangleKeyboardFabric;
import com.smartru.telegram.commands.geometric.keyboards.fabric.ShapeKeyboardFabric;
import com.smartru.telegram.model.UserProblemContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.ParseException;
import java.util.Arrays;

/**
 * TODO: При отправке сообщения бот добавляет в наччале сообщения свое имя!
 */
@Slf4j
@Component
public class GeometrySolveCommand implements IBotCommand {

    @Autowired
    @Qualifier("geometricCalculator")
    private Calculator calculator;
    private final String IDENTIFIER = "solve";
    private final String DESCRIPTION = "Решение геометрической задачи";

    private final ThreadLocal<Long> localUserId = new ThreadLocal<>();
    private final ThreadLocal<AbsSender>localSender = new ThreadLocal<>();
    private final ThreadLocal<Message>localMessage = new ThreadLocal<>();

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        localSender.set(absSender);
        localMessage.set(message);
        sendShapeSelectionKeyboard();
    }

    /**
     * TODO: Переделать switch в обычный if с условиями. Перевести комментарии в названия методов - условий.
     * @param absSender
     * @param update
     */
    public void processCallBack(AbsSender absSender, Update update) {
        String[] args = argsFromUpdate(update);
        System.out.println(Arrays.deepToString(args));
        addUserInContext(update);
        localSender.set(absSender);
        localMessage.set(update.getCallbackQuery().getMessage());
        localUserId.set(update.getCallbackQuery().getFrom().getId());

        switch (args.length) {
            // Один аргумент - значит, пользователь выбрал "рабочую" фигуру.
            // Отправляем клавиатуру с возможными неизвестными
            case 1: {
                sendShapeKeyboard(args[0]);
                break;
            }
            // Два аргумента - значит, пользователь выбрал неизвестное.
            // Отправляем клавиатуру с возможными известными
            case 2: {
                sendShapeParameterKeyboard(args[0], args[1]);
                break;
            }
            // Три аргумента - конец ввода данных по задаче.
            case 3: {
                if (!args[2].equals("end")){
                    return;
                }
                solveProblem();
                break;
            }
            // Четыре аргумента - значит, пользователь выбрал известный ему параметр
            case 4: {
                if (args[3].equals("known")){
                    addParameter(absSender,update.getCallbackQuery().getMessage(), args[2]);
                }
                break;
            }
            default:{
                break;
            }
        }
    }

    private void sendShapeSelectionKeyboard(){
        ReplyKeyboard keyboard = SelectShapeKeyboard.get();
        SendMessage message = prepareMessageWithKeyboard("Какая ключевая фигура в задаче?", keyboard);
        Util.sendToTelegram(localSender.get(), message);
    }

    private void sendShapeKeyboard(String shapeType){
        setShapeToProblemCache(shapeType);
        ShapeKeyboardFabric keyboardFabric = getKeyboardFabricForShape(shapeType);

        ReplyKeyboard shapeKeyboard = keyboardFabric.getShapeKeyboard();
        EditMessageText message = prepareEditMessageWithKeyboard("Что ты хочешь найти?", (InlineKeyboardMarkup) shapeKeyboard);
        Util.sendEditMessage(localSender.get(), message);
    }

    private void sendShapeParameterKeyboard(String shapeType, String unknownProperty){
        setUnknownPropertyToProblemCache(unknownProperty);
        ShapeKeyboardFabric keyboardFabric = getKeyboardFabricForShape(shapeType);
        Desired desiredProperty = Shape.Type.get(shapeType).getDesired(unknownProperty);

        ReplyKeyboard shapeParameterKeyboard = keyboardFabric.getShapeParameterKeyboard(desiredProperty);
        EditMessageText message = prepareEditMessageWithKeyboard("Что тебе известно?", (InlineKeyboardMarkup) shapeParameterKeyboard);
        Util.sendEditMessage(localSender.get(), message);
    }

    public void setParameterInProblemCache(Message message){
        String parameter = UserProblemContext.get(message.getFrom().getId()).getLastProp();
        UserProblemContext.get(message.getFrom().getId()).getProps().put(parameter, message.getText());
        sendMoreParametersKeyboard(UserProblemContext.get(message.getFrom().getId()).getShape(),
                UserProblemContext.get(message.getFrom().getId()).getUnknown());
    }

    private void sendMoreParametersKeyboard(String shapeType, String unknownProperty){
        ShapeKeyboardFabric keyboardFabric = getKeyboardFabricForShape(shapeType);
        Desired desiredProperty = Shape.Type.get(shapeType).getDesired(unknownProperty);
        ReplyKeyboard shapeParameterKeyboard = keyboardFabric.getShapeParameterKeyboard(desiredProperty);

        SendMessage message = prepareMessageWithKeyboard("Что еще тебе известно?", shapeParameterKeyboard);
        Util.sendToTelegram(localSender.get(), message);
    }

    private void solveProblem(){
        try {
            setProblemIsEquipped();
            String problem = UserProblemContext.get(localUserId.get()).getGeometricProblemFromCache();
            System.out.println(problem);
            double result = calculator.solve(problem);
            System.out.println(result);
            String answer = createResultAnswer(result);
            SendMessage msg = prepareMessage(answer);
            Util.sendToTelegram(localSender.get(),msg);
            UserProblemContext.clearUserCache(localUserId.get());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addParameter(AbsSender sender, Message message, String lastProp){
        setLastPropToProblemCache(lastProp);
        String lastPropRusName = getRusName(lastProp);
        SendMessage msg = prepareMessage("введи: "+lastPropRusName);
        Util.sendToTelegram(sender,msg);
    }

    private SendMessage prepareMessageWithKeyboard(String text, ReplyKeyboard keyboard){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(localMessage.get().getChatId()));
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private EditMessageText prepareEditMessageWithKeyboard(String text, InlineKeyboardMarkup keyboard){
        EditMessageText message = new EditMessageText();
        message.setMessageId(localMessage.get().getMessageId());
        message.setChatId(String.valueOf(localMessage.get().getChatId()));
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private SendMessage prepareMessage(String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(localMessage.get().getChatId()));
        sendMessage.setText("@"+usernameFromContext()+", "+text);
        return sendMessage;
    }

    private ShapeKeyboardFabric getKeyboardFabricForShape(String shapeType){
        if(Shape.Type.CIRCLE.name().equals(shapeType)){
            return new CircleKeyboardFabric();
        } else if (Shape.Type.RECTANGLE.name().equals(shapeType)){
            return new RectangleKeyboardFabric();
        } else {
            throw new IllegalArgumentException("Неизвестный тип фигуры");
        }
    }

    private String getRusName(String property){
        String shapeString = UserProblemContext.get(localUserId.get()).getShape();
        Shape shape = Shape.Type.get(shapeString);
        return shape.getDesired(property).getRusName();
    }

    private String createResultAnswer(double problemResult){
        if (problemResult==-1){
            return "Недостаточно данных";
        } else {
            String desireProp = UserProblemContext.get(localUserId.get()).getUnknown();
            String desirePropRusName = getRusName(desireProp);
            return desirePropRusName+" = "+problemResult;
        }
    }

    private String[] argsFromUpdate(Update update){
        return update.getCallbackQuery().getData().replace("/solve ","").split(" ");
    }

    private void addUserInContext(Update update){
        UserProblemContext.add(update.getCallbackQuery().getFrom());
    }

    private void setShapeToProblemCache(String shapeType){
        UserProblemContext.get(localUserId.get()).setShape(shapeType);
    }

    private void setUnknownPropertyToProblemCache(String unknownProperty){
        UserProblemContext.get(localUserId.get()).setUnknown(unknownProperty);
    }

    private void setLastPropToProblemCache(String lastProp){
        UserProblemContext.get(localUserId.get()).setLastProp(lastProp);
    }

    private void setProblemIsEquipped(){
        UserProblemContext.get(localUserId.get()).setEquipped(true);
    }

    private String usernameFromContext(){
        return UserProblemContext.get(localUserId.get()).getUsername();
    }

    @Override
    public String getCommandIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
