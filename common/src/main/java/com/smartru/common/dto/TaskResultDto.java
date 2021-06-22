package com.smartru.common.dto;

import com.smartru.common.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskResultDto {

    private final long taskId;
    private final String num;
    private final String result;

    public static TaskResultDto fromTask(Task task){
        TaskResultDto resultDto = new TaskResultDto(
                task.getId(),
                task.getNum(),
                resultFromTask(task));
        return resultDto;
    }

    private static String resultFromTask(Task task){
        if (task.getResult()==null){
            return "Task yet not perform";
        }
        else if (task.getResult().isPrime()==false){
            return "Num is not prime";
        }
        else if (task.getResult().isPrime()==true){
            return "Num is prime!";
        }
        else {
            return null;
        }
    }
}
