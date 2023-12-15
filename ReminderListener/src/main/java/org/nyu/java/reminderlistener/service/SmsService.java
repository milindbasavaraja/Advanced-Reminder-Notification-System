package org.nyu.java.reminderlistener.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.nyu.java.reminderlistener.model.Reminder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private VonageClient vonageClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendSms(String msg) {

        try {
            Reminder reminder = objectMapper.readValue(msg, Reminder.class);
            String smsBody = String.format("Reminder Name: %s\nDescription: %s\nDate: %s\nTime: %s",
                    reminder.getReminderName().trim(),
                    reminder.getReminderDescription().trim(),
                    reminder.getReminderDate(),
                    reminder.getReminderTime());
            TextMessage message = new TextMessage("12342414513", reminder.getReminderToEvent().substring(1).trim(),
                    smsBody);

            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                logger.info("Message sent successfully.");
            } else {
                logger.info("Message failed with error: " + response.getMessages().get(0).getErrorText());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

}
