package com.where.email.service;

import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendSignUpConfirmationEmail(String to, String name,String link);
    public String handleMessagingException(MessagingException e);
}
