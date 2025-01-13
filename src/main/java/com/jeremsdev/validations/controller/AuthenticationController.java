package com.jeremsdev.validations.controller;

import com.jeremsdev.validations.auth.AuthenticationService;
import com.jeremsdev.validations.auth.RegisterRequest;
import com.jeremsdev.validations.exception.ApiRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jeremsdev.validations.auth.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        logger.info("Received request to add a new user: {}", request.getName());
        try {
            var response = service.register(request);
            logger.info("User register successfully: {}", request.getName());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error register User: {}", e.getMessage());
            throw new ApiRequestException("Error Register User: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        logger.info("Received request to authenticate user: {}", request.getEmail());
        try {
            var response = service.authenticate(request);
            logger.info("User authenticate successfully: {}", request.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error authenticate User: {}", e.getMessage());
            throw new ApiRequestException("Error authenticate User: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        logger.info("Received request to refresh token");
        try {
            service.refreshToken(request, response);
            logger.info("Token refresh successfully");
        } catch (Exception e) {
            logger.error("Error refresh token: {}", e.getMessage());
            throw new ApiRequestException("Error refresh token: " + e.getMessage());
        }

    }
}
