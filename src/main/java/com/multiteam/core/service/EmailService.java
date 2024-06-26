package com.multiteam.core.service;

import com.multiteam.core.models.EmailVO;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.email.from}")
    private String emailFrom;

    public boolean sendEmailNewUser(EmailVO email) {

        boolean success = true;

        Email to = new Email(email.email().trim());
        Email from = new Email(emailFrom);
        String subject = email.subject();
        Content content = email.content();

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() != 202 || response.getStatusCode() != 200) {
                logger.error("An error occurred when trying to send the first login email to: {}", email);
                success = false;
            }
        } catch (IOException ex) {
            success = false;
            logger.error(ex.getStackTrace());
        }
        return success;
    }
}
