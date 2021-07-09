package com.smartru.telegram.commands.geometric.keyboards.fabric;

import com.smartru.performers.geometric.shape.Shape;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ButtonFabric {

    public static InlineKeyboardButton createButton(String name, String callbackData){
        InlineKeyboardButton square = new InlineKeyboardButton();
        square.setText(name);
        square.setCallbackData(callbackData);
        return square;
    }
}
