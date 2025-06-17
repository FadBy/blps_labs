package com.blps.lab1.jca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;

@Configuration
public class MailJcaConfig {

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Bean
    public MailConnectionFactory mailConnectionFactory() {
        MailManagedConnectionFactory mcf = new MailManagedConnectionFactory();
        mcf.setApiKey(smtpHost);     // 👈 Важно!
        mcf.setSmtpPort(smtpPort);
        mcf.setUsername(username);
        mcf.setPassword(password);

        ConnectionManager cm = new ConnectionManager() {
            @Override
            public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
                ManagedConnection mc = mcf.createManagedConnection(null, cxRequestInfo);
                return mc.getConnection(null, cxRequestInfo);
            }
        };

        return new MailConnectionFactory(mcf, cm);
    }
}
