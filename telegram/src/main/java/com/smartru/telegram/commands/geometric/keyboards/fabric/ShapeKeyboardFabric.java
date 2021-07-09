package com.smartru.telegram.commands.geometric.keyboards.fabric;

import com.smartru.performers.geometric.shape.Desired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public abstract class ShapeKeyboardFabric {

    public abstract ReplyKeyboard getShapeKeyboard();
    public abstract ReplyKeyboard getShapeParameterKeyboard(Desired desired);
}
