package com.blps.lab1.jca;

public interface MailConnection {
    void sendEmail(String to, String subject, String body);
}
