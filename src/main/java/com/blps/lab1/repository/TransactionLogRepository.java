package com.blps.lab1.repository;

import com.blps.lab1.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
} 