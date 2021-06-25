package com.smartru.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class TelegramTask{

    private final long chatId;
    private final Task task;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TelegramTask(@JsonProperty("task")Task task,
                        @JsonProperty("chatId")long chatId) {
        this.task = task;
        this.chatId = chatId;
    }

    public long getId(){
        return task.getId();
    }

    public TaskResult getResult(){
        return task.getResult();
    }

    public void setResult(TaskResult result){
        task.setResult(result);
    }
}
