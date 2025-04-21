package com.blps.lab1.controllers;

import com.blps.lab1.dto.AuthResponse;
import com.blps.lab1.dto.LoginRequest;
import com.blps.lab1.dto.RegisterRequest;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.RequestInvalidException;
import com.blps.lab1.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
