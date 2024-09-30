package com.ms.cronscheduler.service;

// EmailService.java
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class EmailService {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    public void sendEmailWithAttachment(byte[] attachment, String fileName,String[] toEmails, String[] ccEmails,String body, String theSubject) throws IOException {
        Mail mail = new Mail();

        Email fromEmail = new Email("harikrishna.t@olamnet.com");
        mail.setFrom(fromEmail);

        mail.setSubject(theSubject);

        Personalization personalization = new Personalization();

        // Add multiple "To" recipients
        for (String toEmail : toEmails) {
            personalization.addTo(new Email(toEmail));
        }

        // Add "CC" recipients if provided
        if (ccEmails != null && ccEmails.length != 0) {
            for (String ccEmail : ccEmails) {
                personalization.addCc(new Email(ccEmail));
            }
        }

        mail.addPersonalization(personalization);

        Content content = new Content("text/plain", body);
        mail.addContent(content);
        Attachments attachments = new Attachments();
        attachments.setContent(Base64.getEncoder().encodeToString(attachment));
        attachments.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        attachments.setFilename(fileName);
        attachments.setDisposition("attachment");
        mail.addAttachments(attachments);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }
}
