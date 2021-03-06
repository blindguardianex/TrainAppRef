package com.smartru.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "task_results")
@NoArgsConstructor
@AllArgsConstructor
public class TaskResult extends BaseEntity{

    @NotNull
    @JsonIgnore
    @Access(AccessType.FIELD)
    @OneToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @Access(AccessType.FIELD)
    @Column(name = "is_prime", nullable = false)
    private boolean isPrime;

    @Override
    public String toString() {
        return "TaskResult{" +
                "task_id='"+ task.getId() + '\'' +
                "result='" + isPrime + '\'' +
                '}';
    }
}
