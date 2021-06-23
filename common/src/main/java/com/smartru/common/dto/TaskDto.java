package com.smartru.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartru.common.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TaskDto {

    private final long taskId;
    private final String num;
    private final String result;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    private TaskDto(@JsonProperty("taskId") long taskId,
                    @JsonProperty("num") String num,
                    @JsonProperty("result") String result) {
        this.taskId = taskId;
        this.num = num;
        this.result = result;
    }

    public static TaskDto fromTask(Task task){
        TaskDto resultDto = new TaskDto(
                task.getId(),
                task.getNum(),
                resultFromTask(task));
        return resultDto;
    }

    private static String resultFromTask(Task task){
        if (task.getResult()==null){
            return "Task yet not perform";
        }
        else if (task.getResult().isPrime()==true){
            return "Num is prime!";
        }
        else {
            return "Num is not prime";
        }
    }
}
