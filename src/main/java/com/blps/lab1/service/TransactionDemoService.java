package com.blps.lab1.service;

import com.blps.lab1.entity.TransactionLog;
import com.blps.lab1.repository.TransactionLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionDemoService {

    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public void performSuccessfulTransaction() {
        // Создаем лог успешной транзакции
        TransactionLog log = new TransactionLog();
        log.setOperation("SUCCESSFUL_OPERATION");
        log.setStatus("COMMITTED");
        transactionLogRepository.save(log);
    }

    @Transactional
    public void performFailedTransaction() {
        // Создаем лог неуспешной транзакции
        TransactionLog log = new TransactionLog();
        log.setOperation("FAILED_OPERATION");
        log.setStatus("ROLLED_BACK");
        transactionLogRepository.save(log);
        
        // Вызываем исключение для отката транзакции
        throw new RuntimeException("Transaction failed");
    }

    @Transactional
    public void performNestedTransaction() {
        // Создаем лог для внешней транзакции
        TransactionLog outerLog = new TransactionLog();
        outerLog.setOperation("OUTER_TRANSACTION");
        outerLog.setStatus("IN_PROGRESS");
        transactionLogRepository.save(outerLog);

        try {
            // Вызываем метод с вложенной транзакцией
            performSuccessfulTransaction();
            
            // Обновляем статус внешней транзакции
            outerLog.setStatus("COMMITTED");
            transactionLogRepository.save(outerLog);
        } catch (Exception e) {
            // В случае ошибки обновляем статус
            outerLog.setStatus("ROLLED_BACK");
            transactionLogRepository.save(outerLog);
            throw e;
        }
    }
} 