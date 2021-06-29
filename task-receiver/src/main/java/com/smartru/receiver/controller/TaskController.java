package com.smartru.receiver.controller;

import com.smartru.common.dto.TaskDto;
import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.common.service.jpa.UserService;
import com.smartru.common.service.rabbitmq.TaskRabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/tasks",
        consumes="application/json")
public class TaskController {

    private final UserService userService;
    private final TaskService taskService;
    private final TaskRabbitService taskRabbitService;

    @Autowired
    public TaskController(
            UserService userService,
            TaskService taskService,
            TaskRabbitService taskRabbitService) {
        this.userService = userService;
        this.taskService = taskService;
        this.taskRabbitService = taskRabbitService;
    }

    @PostMapping()
    public ResponseEntity<TaskDto> addTask(@Valid @RequestBody Task task, Principal principal){
        User user = userService.getByUsername(principal.getName()).get();
        task.setUser(user);
        task = taskService.add(task);
        taskRabbitService.sendTask(task);
        return new ResponseEntity(TaskDto.fromTask(task), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") long taskId, Principal principal){
        Optional<Task> optTask = taskService.getById(taskId);
        if (optTask.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task = optTask.get();
        if (!userOwnsTask(task, principal.getName()) || !taskIsActive(task)){
            log.warn("Trying to get someone else's task or not active task by: {}",principal.getName());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(TaskDto.fromTask(task), HttpStatus.OK);
    }

    @GetMapping()
    public List<TaskDto> getTasks(Principal principal){
        List<Task>tasks = taskService.getAllTasksByUser(principal.getName());
        return tasks.stream()
                .map(TaskDto::fromTask)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable("id") long taskId, Principal principal){
        Optional<Task> optTask = taskService.getById(taskId);
        if (optTask.isPresent()){
            Task task = optTask.get();
            if (userOwnsTask(task, principal.getName())) {
                taskService.setDeletedStatus(task);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                log.warn("Trying to delete someone else's task by: {}",principal.getName());
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean userOwnsTask(Task task, String username){
        return !task.getUser().getLogin().equals(username);
    }

    private boolean taskIsActive(Task task){
        return task.getStatus().equals(BaseEntity.Status.ACTIVE);
    }
}
