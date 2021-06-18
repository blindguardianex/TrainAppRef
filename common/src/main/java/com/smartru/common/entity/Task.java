package com.smartru.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
public class Task extends BaseEntity{

    @NotBlank
    @Access(AccessType.FIELD)
    @Column(name = "task_body", nullable = false)
    private String taskBody;

    @OneToOne(targetEntity = TaskResult.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "result")
    private TaskResult result;

    public Task(String taskBody) {
        this.taskBody = taskBody;
    }
}
