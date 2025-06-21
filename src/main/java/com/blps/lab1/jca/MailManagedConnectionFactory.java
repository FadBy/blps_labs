package com.blps.lab1.jca;

import lombok.Setter;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

@Setter
public class MailManagedConnectionFactory implements ManagedConnectionFactory {

    private String smtpHost;
    private int smtpPort;
    private String username;
    private String password;

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) {
        return new MailConnectionFactory(this, cxManager);
    }

    @Override
    public Object createConnectionFactory() {
        return new MailConnectionFactory(this, null);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) {
        return new MailManagedConnection(smtpHost, smtpPort, username, password);
    }

    @Override
    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() { return null; }
    @Override
    public void setLogWriter(PrintWriter out) {}
    @Override
    public boolean equals(Object obj) { return obj instanceof MailManagedConnectionFactory; }
    @Override
    public int hashCode() { return smtpHost.hashCode(); }
}

