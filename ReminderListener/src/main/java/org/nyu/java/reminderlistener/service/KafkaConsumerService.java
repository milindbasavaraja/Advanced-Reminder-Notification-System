package org.nyu.java.reminderlistener.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @KafkaListener(topics = "reminder_topic", groupId = "group-1")
    public void listen(ConsumerRecord<String, String> record) {
        int partition = record.partition();
        if (partition == 0) {
            emailService.sendEmail(record.value());
        } else if (partition == 2) {
            smsService.sendSms(record.value());
        }

    }
}
