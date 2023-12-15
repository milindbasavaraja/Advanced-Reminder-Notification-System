package org.nyu.java.project.reminderregister.config;

import org.nyu.java.project.reminderregister.entity.ReminderEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Comparator;
import java.util.Properties;
import java.util.concurrent.PriorityBlockingQueue;

@Configuration
public class AppConfig {

    @Bean
    public PriorityBlockingQueue<ReminderEntity> reminderQueue() {
        return new PriorityBlockingQueue<>(10, Comparator.comparing(ReminderEntity::getReminderTime));
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("milindaws1@gmail.com");
        mailSender.setPassword("mucb antu gbcn mxxo");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


}