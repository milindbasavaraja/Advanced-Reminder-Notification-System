package org.nyu.java.project.reminderregister.service;

import com.nyu.java.Reminder;
import org.nyu.java.project.reminderregister.entity.ReminderEntity;
import org.nyu.java.project.reminderregister.repository.ReminderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class ReminderProcessor implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ReminderProcessor.class);


    private final PriorityBlockingQueue<ReminderEntity> expiringReminders;


    private final ReminderRepository reminderRepository;


    private final KafkaTemplate<String, String> kafkaTemplate;

    public ReminderProcessor(PriorityBlockingQueue<ReminderEntity> expiringReminders, ReminderRepository reminderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.expiringReminders = expiringReminders;
        this.reminderRepository = reminderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run(String... args) {
        new Thread(this::processReminders).start();
    }

    private void processReminders() {
        try {
            while (true) {
                logger.info("Polling Recent Expiring Reminders:{}", expiringReminders.size());

                processExpiringReminders();
                Thread.sleep(60000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Exception occurred: {}", e.getMessage(), e);
        }
    }

    private void processExpiringReminders() {
        LocalTime expirationTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (!expiringReminders.isEmpty()) {
            logger.info("The times are: {}->{}:{ }", expiringReminders.peek().getReminderTime(), expirationTime, expiringReminders.peek().getReminderTime().equals(expirationTime));
        }

        while (!expiringReminders.isEmpty() &&
                expiringReminders.peek().getReminderTime().isBefore(expirationTime.plusMinutes(1))) {
            ReminderEntity reminder = expiringReminders.poll();
            sendToKafka(reminder);
            updateReminderAsExpired(reminder);
        }
    }

    private void sendToKafka(ReminderEntity reminderEntity) {
        int partitionKey = getPartitionKey(reminderEntity.getReminderWay().toLowerCase());
        Reminder reminder = Reminder.newBuilder()
                .setReminderName(reminderEntity.getReminderName())
                .setReminderDescription(reminderEntity.getReminderDescription())
                .setReminderDate(reminderEntity.getReminderDate().toString())
                .setReminderTime(reminderEntity.getReminderTime().toString())
                .setReminderWay(reminderEntity.getReminderWay())
                .setReminderToEvent(reminderEntity.getReminderToEvent())
                .build();
        logger.info("Sending reminder to partition: {}, Reminder: {}", partitionKey, reminder);
        kafkaTemplate.send("reminder_topic", partitionKey, "key", reminder.toString());
    }

    private void updateReminderAsExpired(ReminderEntity reminder) {
        reminder.setExpired(true);
        reminderRepository.save(reminder);
        logger.info("Updated reminder as expired: {}", reminder);
    }

    private int getPartitionKey(String reminderWay) {
        switch (reminderWay.trim().toLowerCase()) {
            case "email":
                return 0;
            case "message":
                return 2;
            default:
                return 1;
        }
    }
}
