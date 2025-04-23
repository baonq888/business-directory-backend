package com.where.email.service;

import com.where.email.kafka.business.BusinessStatusUpdateEvent;
import com.where.email.kafka.register.RegisterTokenEvent;

public interface EmailSender {
    void sendSignUpConfirmationEmail(RegisterTokenEvent registerTokenEvent);
    void sendBusinessStatusUpdatedEmail(BusinessStatusUpdateEvent businessStatusUpdateEvent);
}
