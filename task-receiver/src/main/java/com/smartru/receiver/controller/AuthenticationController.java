package com.smartru.receiver.controller;

import com.smartru.common.dto.AuthenticationRequestDto;
import com.smartru.common.dto.RegistrationRequestDto;
import com.smartru.common.entity.User;
import com.smartru.common.exceptions.EntityAlreadyExists;
import com.smartru.common.service.jpa.UserService;
import com.smartru.receiver.configuration.security.RegistrationProvider;
import com.smartru.receiver.configuration.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/",
        consumes="application/json")
public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RegistrationProvider registrationProvider;

    @Autowired
    public AuthenticationController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager,
                                    UserService userService, RegistrationProvider registrationProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.registrationProvider = registrationProvider;
    }

    @PostMapping("sign")
    public ResponseEntity signIn(@RequestBody AuthenticationRequestDto auth){
        String username = auth.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, auth.getPassword()));
        User user = userService.getByUsername(username).get();

        Map<Object, Object> response = jwtTokenProvider.createTokens(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh_token")
    public ResponseEntity refreshToken(@CookieValue(value = "Refresh_Token") String refreshToken,
                                       @CookieValue(value = "Expired_Token") String expiredToken){
        try{
            Map<Object, Object>response = jwtTokenProvider.refreshTokens(refreshToken, expiredToken);
            log.info("Token refresh is successful");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | JwtException ex) {
            log.error("Incorrect refresh token");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

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

