package com.smartru.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
public class Task extends BaseEntity{

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @NotBlank
    @Access(AccessType.FIELD)
    @Pattern(regexp = "\\A\\d*\\Z", message = "Введенная строка не является числом")
    @Column(name = "num", nullable = false)
    private String num;

    @OneToOne(targetEntity = TaskResult.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "result")
    private TaskResult result;

    public Task(String num) {
        this.num = num;
    }
}
