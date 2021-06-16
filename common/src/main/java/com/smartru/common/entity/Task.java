package com.smartru.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
public class Task extends BaseEntity{

    @NotBlank
    @Column(name = "task_body")
    private String taskBody;

    @OneToOne(targetEntity = TaskResult.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "result")
    private TaskResult result;

    public Task(String taskBody) {
        this.taskBody = taskBody;
    }
}
