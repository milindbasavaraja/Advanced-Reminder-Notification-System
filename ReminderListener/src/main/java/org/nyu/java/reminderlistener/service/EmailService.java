package org.nyu.java.reminderlistener.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nyu.java.reminderlistener.model.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendEmail(String message) {
        try {
            Reminder reminder = objectMapper.readValue(message, Reminder.class);
            String emailBody = String.format("Reminder Name: %s\nDescription: %s\nDate: %s\nTime: %s",
                    reminder.getReminderName().trim(),
                    reminder.getReminderDescription().trim(),
                    reminder.getReminderDate(),
                    reminder.getReminderTime());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(new String[]{reminder.getReminderToEvent().trim()});

            mailMessage.setSubject("Reminder: " + reminder.getReminderName().trim());
            mailMessage.setText(emailBody);
            mailSender.send(mailMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
