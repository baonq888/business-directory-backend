package com.where.email.service.helper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.Map;

import static com.where.email.service.EmailTemplates.BUSINESS_STATUS_UPDATED;
import static com.where.email.service.EmailTemplates.REGISTER_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public class EmailSenderHelper {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${email.from}")
    private String fromEmail;

    public void sendEmail(
            String to,
            Map<String, Object> variables,
            String templateName,
            String emailSubject
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        Context context = new Context();
        context.setVariables(variables);

        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                UTF_8.name()
        );
        messageHelper.setFrom(fromEmail);
        messageHelper.setSubject(emailSubject);

        String htmlTemplate = templateEngine.process(templateName, context);
        messageHelper.setText(htmlTemplate, true);
        messageHelper.setTo(to);

        mailSender.send(mimeMessage);
    }
}
