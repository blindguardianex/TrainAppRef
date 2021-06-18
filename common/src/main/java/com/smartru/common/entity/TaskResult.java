package com.smartru.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Сущность-результат выполнения Task
 * @see com.smartru.common.entity.Task
 */
@Data
@Entity
@Table(name = "task_results")
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Immutable
public class TaskResult extends BaseEntity{

    @NotNull
    @JsonIgnore
    @Access(AccessType.FIELD)
    @OneToOne(targetEntity = Task.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @Access(AccessType.FIELD)
    @Column(name = "result_body", nullable = false)
    private String result;

    @Override
    public String toString() {
        return "TaskResult{" +
                "task_id='"+ task.getId() + '\'' +
                "result='" + result + '\'' +
                '}';
    }
}
