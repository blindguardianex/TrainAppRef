package com.smartru.receiver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.common.dto.AuthenticationRequestDto;
import com.smartru.common.dto.RegistrationRequestDto;
import com.smartru.common.service.jpa.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@DirtiesContext
@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    private MockMvc mvc;
    private UserService userService;
    private ObjectMapper mapper;

    private final String AUTHORIZATION_ENDPOINT = "/api/sign";
    private final AuthenticationRequestDto SUCCESSFUL_AUTHENTICATION_DTO = new AuthenticationRequestDto("testy", "zaq123");
    private final AuthenticationRequestDto WRONG_LOGIN_AUTHENTICATION_DTO = new AuthenticationRequestDto("testqwe", "zaq123");
    private final AuthenticationRequestDto WRONG_PASSWORD_AUTHENTICATION_DTO = new AuthenticationRequestDto("testy", "zaaasdq123");

    private final String REFRESH_TOKEN_ENDPOINT = "/api/refresh_token";

    @Autowired
    public AuthenticationControllerTest(MockMvc mvc, UserService userService, ObjectMapper mapper) {
        this.mvc = mvc;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Test
    void signIn() throws Exception {
        log.info("Starting authorization endpoint test...");
        System.out.println();

        log.info("Test #1: successful authorization...");
        assertTrue(checkUserInBase(SUCCESSFUL_AUTHENTICATION_DTO.getUsername()));
        mvc.perform(post(AUTHORIZATION_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(SUCCESSFUL_AUTHENTICATION_DTO)))
//                .andDo(print())
                .andExpect(status().isOk());
        log.info("Test #1 completed successfully");
        System.out.println("\n");

        log.info("Test #2: failed authorization by \"wrong login\"");
        assertFalse(checkUserInBase(WRONG_LOGIN_AUTHENTICATION_DTO.getUsername()));
        mvc.perform(post(AUTHORIZATION_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(WRONG_LOGIN_AUTHENTICATION_DTO)))
//                .andDo(print())
                .andExpect(status().is(401));
        log.info("Test #2 completed successfully");
        System.out.println("\n");

        log.info("Test #3: failed authorization by \"wrong password\"");
        assertNotEquals(SUCCESSFUL_AUTHENTICATION_DTO.getPassword(), WRONG_PASSWORD_AUTHENTICATION_DTO.getPassword());
        mvc.perform(post(AUTHORIZATION_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(WRONG_PASSWORD_AUTHENTICATION_DTO)))
//                .andDo(print())
                .andExpect(status().is(401));
        log.info("Test #3 completed successfully");
    }

    @Test
    void refreshToken() throws Exception {
        log.info("Starting refresh token endpoint test...");
        System.out.println();

        log.info("Getting access and refresh tokens...");
        assertTrue(checkUserInBase(SUCCESSFUL_AUTHENTICATION_DTO.getUsername()));
        MvcResult auth = mvc.perform(post(AUTHORIZATION_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(SUCCESSFUL_AUTHENTICATION_DTO)))
//                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String accessToken = "Bearer "+accessTokenFromMvcResult(auth);
        String refreshToken = refreshTokenFromMvcResult(auth);
        log.info("Successfully authorization!\nAccess token is: {}\nRefresh token is: {}", accessToken, refreshToken);
        System.out.println("\n");

        Cookie cookieRefreshToken = new Cookie("Refresh_Token",refreshToken);
        Cookie cookieAccessToken = new Cookie("Expired_Token",accessToken);

        log.info("Test #1: failed refresh token by \"wrong access token\"");
        Cookie failedCookieAccessToken = new Cookie("Expired_Token",accessToken.substring(5));
        mvc.perform(post(REFRESH_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookieRefreshToken, failedCookieAccessToken))
                    .andExpect(status().is(403));
        log.info("Test #1 completed successfully");
        System.out.println("\n");

        log.info("Test #2: failed refresh token by \"wrong refresh token\"");
        Cookie failedCookieRefreshToken = new Cookie("Refresh_Token",refreshToken.substring(5));
        mvc.perform(post(REFRESH_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(failedCookieRefreshToken, cookieAccessToken))
                    .andExpect(status().is(403));
        log.info("Test #2 completed successfully");
        System.out.println("\n");

        log.info("Test #3: successful refresh tokens");
        MvcResult refreshed = mvc.perform(post(REFRESH_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookieRefreshToken, cookieAccessToken))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
        String refreshAccessToken = "Bearer_"+accessTokenFromMvcResult(refreshed);
        String refreshRefreshToken = refreshTokenFromMvcResult(refreshed);
        log.info("Successfully refreshed!\nNew access token is: {}\nNew refresh token is: {}", refreshAccessToken, refreshRefreshToken);
        assertNotEquals(accessToken,refreshAccessToken);
        assertNotEquals(refreshToken,refreshRefreshToken);
        log.info("Test #3 completed successfully");
    }

    private boolean checkUserInBase(String login){
        return userService.getByUsername(login).isPresent();
    }

    private String accessTokenFromMvcResult(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        JsonNode node = mapper.readTree(response.getResponse().getContentAsString());
        String accessToken = node.get("accessToken").asText().replaceAll("\"","");
        return accessToken;
    }

    private String refreshTokenFromMvcResult(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        JsonNode node = mapper.readTree(response.getResponse().getContentAsString());
        String accessToken = node.get("refreshToken").asText().replaceAll("\"","");
        return accessToken;
    }
}