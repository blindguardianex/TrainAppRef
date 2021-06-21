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
    private final boolean isPrime;

    public static TaskResultDto fromTask(Task task){
        TaskResultDto resultDto = new TaskResultDto(
                task.getId(),
                task.getNum(),
                task.getResult().isPrime());
        return resultDto;
    }
}
