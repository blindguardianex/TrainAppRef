package com.smartru.receiver.controller;

import com.smartru.common.dto.RegistrationRequestDto;
import com.smartru.common.exceptions.EntityAlreadyExists;
import com.smartru.receiver.configuration.security.RegistrationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping(value = "/api/",
        consumes="application/json")
public class RegistrationController {

    @Autowired
    private RegistrationProvider registrationProvider;

    @PostMapping("signUp")
    public ResponseEntity registration(@Valid @RequestBody RegistrationRequestDto registrationRequest){
        try {
            log.info("Registration new user: {}", registrationRequest.getUsername());
            registrationProvider.registry(registrationRequest.toUser());
            log.info("Successful registration user: {}", registrationRequest.getUsername());
            return new ResponseEntity(HttpStatus.OK);
        } catch (EntityAlreadyExists ex) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
