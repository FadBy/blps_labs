package com.blps.lab1.controllers;

import com.blps.lab1.entities.Application;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.RequestInvalidException;
import com.blps.lab1.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_APPLICATION')")
    public ResponseEntity<List<Application>> listNew() {
        List<Application> apps = applicationService.getNewApplications();
        return ResponseEntity.ok(apps);
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('APPROVE_APPLICATION')")
    public ResponseEntity<Application> approve(@RequestParam String name) {
        Application app = applicationService.approve(name);
        return ResponseEntity.ok(app);
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('APPROVE_APPLICATION')")
    public ResponseEntity<Application> reject(@RequestParam String name) {
        Application app = applicationService.reject(name);
        return ResponseEntity.ok(app);
    }

    @ExceptionHandler(RequestInvalidException.class)
    public ResponseEntity<ErrorMessage> handleException(RequestInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ex.getMessage()));
    }
}
