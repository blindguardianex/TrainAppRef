package com.smartru.receiver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.common.dto.RegistrationRequestDto;
import com.smartru.common.service.jpa.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
@DirtiesContext
@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {

    private MockMvc mvc;
    private UserService userService;
    private ObjectMapper mapper;

    private final String REGISTRATION_ENDPOINT = "/api/signUp";
    private final RegistrationRequestDto SUCCESSFUL_REGISTRATION_DTO = new RegistrationRequestDto("Security", "qwerty123");
    private final RegistrationRequestDto ALREADY_EXISTS_REGISTRATION_DTO = new RegistrationRequestDto("testy", "asdqwe123");
    private final RegistrationRequestDto BAD_LOGIN_REGISTRATION_DTO = new RegistrationRequestDto("test", "asdqwe123");
    private final RegistrationRequestDto BAD_PASSWORD_REGISTRATION_DTO = new RegistrationRequestDto("test123", "asd");

    @Autowired
    public RegistrationControllerTest(MockMvc mvc, UserService userService, ObjectMapper mapper) {
        this.mvc = mvc;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Test
    void registration() throws Exception {
        log.info("Starting registration endpoint test...");
        System.out.println();

        log.info("Test #1: successful registration...");
        assertFalse(checkUserInBase(SUCCESSFUL_REGISTRATION_DTO.getUsername()));
        mvc.perform(post(REGISTRATION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(SUCCESSFUL_REGISTRATION_DTO)))
//                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(checkUserInBase(SUCCESSFUL_REGISTRATION_DTO.getUsername()));
        log.info("Test #1 completed successfully");
        System.out.println("\n");

        log.info("Test #2: failed registration by \"user already exists\"");
        assertTrue(checkUserInBase(ALREADY_EXISTS_REGISTRATION_DTO.getUsername()));
        mvc.perform(post(REGISTRATION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ALREADY_EXISTS_REGISTRATION_DTO)))
//                .andDo(print())
                .andExpect(status().is(403));
        log.info("Test #2 completed successfully");
        System.out.println("\n");

        log.info("Test #3: failed registration by \"login size\"");
        assertTrue(BAD_LOGIN_REGISTRATION_DTO.getUsername().length()<5);
        assertTrue(BAD_LOGIN_REGISTRATION_DTO.getPassword().length()>=6);
        assertFalse(checkUserInBase(BAD_LOGIN_REGISTRATION_DTO.getUsername()));
        mvc.perform(post(REGISTRATION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(BAD_LOGIN_REGISTRATION_DTO)))
//                .andDo(print())
                .andExpect(status().is(400));
        log.info("Test #3 completed successfully");
        System.out.println("\n");

        log.info("Test #4: failed registration by \"password size\"");
        assertTrue(BAD_PASSWORD_REGISTRATION_DTO.getUsername().length()>=5);
        assertTrue(BAD_PASSWORD_REGISTRATION_DTO.getPassword().length()<6);
        assertFalse(checkUserInBase(BAD_PASSWORD_REGISTRATION_DTO.getUsername()));
        mvc.perform(post(REGISTRATION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(BAD_PASSWORD_REGISTRATION_DTO)))
//                .andDo(print())
                .andExpect(status().is(400));
        log.info("Test #4 completed successfully");
    }

    private boolean checkUserInBase(String login){
        return userService.getByUsername(login).isPresent();
    }
}