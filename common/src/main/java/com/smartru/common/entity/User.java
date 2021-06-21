package com.smartru.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity{

    @NotBlank
    @Size(min = 5, message = "Минимальная длинна логина: 5 символов")
    @Column(name = "login", nullable = false)
    private String login;

    @JsonIgnore
    @NotBlank
    @Size(min = 6, message = "Минимальная длинна пароля: 6 символов")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

    @JsonIgnore
    @Column(name = "access_token")
    private String accessToken;

    @JsonIgnore
    @Column(name = "refresh_token")
    private String refreshToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Task>tasks = new ArrayList<>();

    @PrePersist
    void init(){
        role = "USER";
        super.init();
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                '}';
    }
}
