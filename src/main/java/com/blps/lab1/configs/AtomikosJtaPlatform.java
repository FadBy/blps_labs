package com.blps.lab1.configs;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AtomikosJtaPlatform extends AbstractJtaPlatform {
    private static final long serialVersionUID = 1L;

    static TransactionManager transactionManager;
    static UserTransaction transaction;

    @Override
    protected TransactionManager locateTransactionManager() {
        log.info("AtomikosJtaPlatform: locating TransactionManager");
        return transactionManager;
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        log.info("AtomikosJtaPlatform: locating UserTransaction");
        return transaction;
    }
}
