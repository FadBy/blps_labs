package com.blps.lab1.controller;

import com.blps.lab1.service.TransactionDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionDemoController {

    private final TransactionDemoService transactionDemoService;

    @PostMapping("/success")
    public ResponseEntity<String> testSuccessfulTransaction() {
        transactionDemoService.performSuccessfulTransaction();
        return ResponseEntity.ok("Transaction completed successfully");
    }

    @PostMapping("/fail")
    public ResponseEntity<String> testFailedTransaction() {
        try {
            transactionDemoService.performFailedTransaction();
            return ResponseEntity.ok("This should not be reached");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Transaction failed as expected: " + e.getMessage());
        }
    }

    @PostMapping("/nested")
    public ResponseEntity<String> testNestedTransaction() {
        try {
            transactionDemoService.performNestedTransaction();
            return ResponseEntity.ok("Nested transaction completed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Nested transaction failed: " + e.getMessage());
        }
    }
} 