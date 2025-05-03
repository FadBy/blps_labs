package com.blps.lab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Transaction management configuration.
 */
@Configuration
@EnableTransactionManagement
public class AtomikosConfiguration {

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        // Use a simple JDBC transaction manager instead of JTA
        return new DataSourceTransactionManager(dataSource);
    }
} 