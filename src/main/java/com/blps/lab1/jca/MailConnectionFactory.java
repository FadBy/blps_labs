package com.blps.lab1.jca;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;

public class MailConnectionFactory {

    private final MailManagedConnectionFactory mcf;
    private final ConnectionManager cm;

    public MailConnectionFactory(MailManagedConnectionFactory mcf, ConnectionManager cm) {
        this.mcf = mcf;
        this.cm = cm;
    }

    public MailConnection getConnection() throws ResourceException {
        return (MailConnection) cm.allocateConnection(mcf, null);
    }
}

