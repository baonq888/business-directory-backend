package com.where.email.service;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    REGISTER_CONFIRMATION("register-confirmation.html", "Confirm your email"),
    BUSINESS_STATUS_UPDATED("business-status-updated", "Business Status Updated"),
    ;

    @Getter
    private final String template;
    @Getter
    private final String subject;


    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
