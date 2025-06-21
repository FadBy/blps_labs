package com.blps.lab1.controllers;

import com.blps.lab1.dto.AuthResponse;
import com.blps.lab1.dto.LoginRequest;
import com.blps.lab1.dto.RegisterRequest;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.RequestInvalidException;
import com.blps.lab1.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final RuntimeService runtimeService;

    @PostMapping("/register")
    public AuthResponse register(RegisterRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", request.getUsername());
        variables.put("password", request.getPassword());
        variables.put("role", request.getRole());

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("RegistrationProcess", variables);

        String token = (String) runtimeService.getVariable(instance.getId(), "token");

        if (token == null) {
            var userExists = (Boolean)runtimeService.getVariable(instance.getId(), "userExists");
            if (userExists) {
                throw new IllegalArgumentException();
            }
            throw new RequestInvalidException("Username is already in use");
        }

        return new AuthResponse(token);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @ExceptionHandler(RequestInvalidException.class)
    public ResponseEntity<ErrorMessage> handleException(RequestInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ex.getMessage()));
    }
}
