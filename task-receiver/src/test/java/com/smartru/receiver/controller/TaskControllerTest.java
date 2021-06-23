package com.smartru.receiver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.common.dto.AuthenticationRequestDto;
import com.smartru.common.dto.TaskDto;
import com.smartru.common.entity.Task;
import com.smartru.common.service.jpa.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@DirtiesContext
@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TaskService taskService;

    private final ObjectMapper mapper = new ObjectMapper();
    private static String ACCESS_TOKEN;
    private final String ADD_TASK_ENDPOINT = "/api/tasks";
    private final String GET_TASKS_ENDPOINT = "/api/tasks";
    private final String GET_TASK_ENDPOINT = "/api/tasks/";
    private final String DELETE_TASK_ENDPOINT = "/api/tasks/";

    private final long INCORRECT_TASK_ID = 0;
    private final Task CORRECT_TASK = new Task("2147483647");
    private final Task[] INCORRECT_TASKS = new Task[]{
            new Task("qweqweq"),
            new Task("12321321332s1"),
            new Task(".,/21321")
    };

    @Test
    void addTask() throws Exception {
        log.info("Starting add task test...");
        if (ACCESS_TOKEN==null){
            authorization();
        }
        System.out.println();

        log.info("Test #1: failed adding by task: {}",INCORRECT_TASKS[0]);
        mvc.perform(post(ADD_TASK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(INCORRECT_TASKS[0]))
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is(400));
        log.info("Test #1 completed successfully");
        System.out.println("\n");

        log.info("Test #2: failed adding by task: {}",INCORRECT_TASKS[1]);
        mvc.perform(post(ADD_TASK_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(INCORRECT_TASKS[1]))
                .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is(400));
        log.info("Test #2 completed successfully");
        System.out.println("\n");

        log.info("Test #3: failed adding by task: {}",INCORRECT_TASKS[2]);
        mvc.perform(post(ADD_TASK_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(INCORRECT_TASKS[2]))
                .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is(400));
        log.info("Test #3 completed successfully");
        System.out.println("\n");

        log.info("Test #4: successfully adding task: {}",CORRECT_TASK);
        mvc.perform(post(ADD_TASK_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CORRECT_TASK))
                .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk());
        log.info("Test #4 completed successfully");
    }

    @Test
    void getTask() throws Exception {
        log.info("Starting get task test...");
        if (ACCESS_TOKEN==null){
            authorization();
        }
        System.out.println();

        log.info("Adding correct task: {}",CORRECT_TASK);
        MvcResult result = mvc.perform(post(ADD_TASK_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(CORRECT_TASK))
                    .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        log.info("Task successfully added");
        System.out.println("\n");

        log.info("Test #1: failed getting task by incorrect id: \"{}\"", INCORRECT_TASK_ID);
        mvc.perform(get(GET_TASK_ENDPOINT+INCORRECT_TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is(404));
        log.info("Test #1 completed successfully");
        System.out.println("\n");

        long correctTaskId = taskIdFromMvcResult(result);
        log.info("Test #2: successfully getting task by id: \"{}\"", correctTaskId);
        mvc.perform(get(GET_TASK_ENDPOINT+correctTaskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk());
        log.info("Test #2 completed successfully");
    }

    @Test
    void getTasks() throws Exception {
        log.info("Starting get tasks test...");
        if (ACCESS_TOKEN==null){
            authorization();
        }
        System.out.println();

        log.info("Adding 3 task...");
        for (int i=0;i<3;i++) {
            mvc.perform(post(ADD_TASK_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(CORRECT_TASK))
                    .header("Authorization", ACCESS_TOKEN))
                    .andExpect(status().isOk());
        }
        log.info("Tasks successfully added");
        System.out.println("\n");

        log.info("Test #1: getting all tasks by user");
        MvcResult result = mvc.perform(get(GET_TASKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        TaskDto[]tasks = taskArrayFromMvcResult(result);
        assertTrue(tasks.length>=3);
        log.info("Test #1 completed successfully");
    }

    @Test
    void deleteTask() throws Exception {
        log.info("Starting delete task test...");
        if (ACCESS_TOKEN==null){
            authorization();
        }
        System.out.println();

        log.info("Adding correct task: {}",CORRECT_TASK);
        MvcResult result = mvc.perform(post(ADD_TASK_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CORRECT_TASK))
                .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        long taskId = taskIdFromMvcResult(result);
        log.info("Task successfully added with id #{}",taskId);
        System.out.println("\n");

        log.info("Test #1: deleting task by id #{}",taskId);
        mvc.perform(delete(DELETE_TASK_ENDPOINT+taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk());

        mvc.perform(get(GET_TASK_ENDPOINT+taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is(403));
        log.info("Test #1 completed successfully");
    }

    private void authorization() throws Exception {
        log.info("Getting authorization...");
        MvcResult auth = mvc.perform(post("/api/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new AuthenticationRequestDto("testy", "zaq123"))))
//                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String accessToken = "Bearer "+accessTokenFromMvcResult(auth);
        log.info("Successfully authorization!\nAccess token is: {}", accessToken);
        ACCESS_TOKEN = accessToken;
        System.out.println("\n");
    }

    private String accessTokenFromMvcResult(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        JsonNode node = mapper.readTree(response.getResponse().getContentAsString());
        String accessToken = node.get("accessToken").asText().replaceAll("\"","");
        return accessToken;
    }

    private long taskIdFromMvcResult(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        TaskDto taskDto = mapper.readValue(response.getResponse().getContentAsString(),TaskDto.class);
//        JsonNode node = mapper.readTree(response.getResponse().getContentAsString());
//        long taskId = node.get("taskId").asLong();
//        return taskId;
        return taskDto.getTaskId();
    }

    private TaskDto[] taskArrayFromMvcResult(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        TaskDto[] tasks = mapper.readValue(response.getResponse().getContentAsString(),TaskDto[].class);
        return tasks;
    }
}