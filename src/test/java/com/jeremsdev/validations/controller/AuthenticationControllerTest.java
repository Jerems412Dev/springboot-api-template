package com.jeremsdev.validations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeremsdev.validations.auth.AuthenticationRequest;
import com.jeremsdev.validations.auth.AuthenticationResponse;
import com.jeremsdev.validations.auth.AuthenticationService;
import com.jeremsdev.validations.auth.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;
    @Autowired
    private ObjectMapper objectMapper;
    private AuthenticationRequest authRequest;
    private RegisterRequest registerRequest1;
    private RegisterRequest registerRequest2;
    private AuthenticationResponse authenticationResponse;


    @BeforeEach
    void setUp() {

        authRequest = new AuthenticationRequest();
        authRequest.setEmail("user1@example.com");
        authRequest.setPassword("strongpassword123");

        registerRequest1 = new RegisterRequest();
        registerRequest1.setName("user1");
        registerRequest1.setEmail("user1@example.com");
        registerRequest1.setPhoneNumber("780000000");
        registerRequest1.setPassword("strongpassword123");

        registerRequest2 = new RegisterRequest();
        registerRequest2.setName("user2");
        registerRequest2.setEmail("user2@example.com");
        registerRequest2.setPhoneNumber("770000000");
        registerRequest2.setPassword("strongpassword123");

        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    @Order(1)
    void shouldUserRegisterOne() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void shouldUserRegisterTwo() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest2)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    void shouldUserAuthenticate() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        this.mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }
}
