package com.blps.lab1.utils;

import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

@Component
public class TransactionHelper {
    
    @Transactional
    public void executeInTransaction(Runnable action) {
        action.run();
    }

    @Transactional
    public <T> T executeInTransaction(TransactionCallback<T> action) {
        return action.execute();
    }

    @FunctionalInterface
    public interface TransactionCallback<T> {
        T execute();
    }
} 