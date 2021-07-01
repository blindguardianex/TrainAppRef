package com.smartru.common.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "tasks")
@TypeDef(
        name = "json",
        typeClass = JsonStringType.class
)
@NoArgsConstructor
public class Task extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @NotBlank
    @Access(AccessType.FIELD)
    @Pattern(regexp = "\\A\\d*\\Z", message = "Введенная строка не является числом")
    @Size(max = 23, message = "Введенное число слишком большое")
    @Column(name = "num", nullable = false)
    private String num;

    @OneToOne(targetEntity = TaskResult.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "result")
    private TaskResult result;

    @org.hibernate.annotations.Type(type = "json")
    @Column(name = "properties", columnDefinition = "json")
    private ObjectNode properties;

    public Task(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Task{" +
                "num='" + num + '\'' +
                ", properties='" + properties + '\'' +
                '}' + super.toString();
    }

    public static enum Type {
        HTTP("http"),
        TELEGRAM("telegram");

        private final String type;

        private Type(String type){
            this.type=type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
