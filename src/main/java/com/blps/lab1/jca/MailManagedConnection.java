package com.blps.lab1.jca;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;


public class MailManagedConnection implements ManagedConnection {
    private final MailConnectionImpl connection;

    public MailManagedConnection(String smtpHost, int smtpPort, String username, String password) {
        this.connection = new MailConnectionImpl(smtpHost, smtpPort, username, password);
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) {
        return connection;
    }

    @Override public void destroy() {}
    @Override public void cleanup() {}
    @Override public void associateConnection(Object connection) {}

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {

    }

    @Override public void removeConnectionEventListener(ConnectionEventListener listener) {}
    @Override public XAResource getXAResource() { return null; }
    @Override public LocalTransaction getLocalTransaction() { return null; }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override public PrintWriter getLogWriter() { return null; }
    @Override public void setLogWriter(PrintWriter out) {}
}
