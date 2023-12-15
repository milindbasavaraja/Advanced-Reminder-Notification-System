package org.nyu.java.project.reminderregister.service;

import org.nyu.java.project.reminderregister.dao.ReminderDao;
import org.nyu.java.project.reminderregister.entity.ReminderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;


@Service
public class ReminderPollingService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ReminderPollingService.class);

    private final PriorityBlockingQueue<ReminderEntity> expiringReminders;


    private final ReminderDao reminderDao;

    public ReminderPollingService(ReminderDao reminderDao, PriorityBlockingQueue<ReminderEntity> expiringReminders) {
        this.expiringReminders = expiringReminders;
        this.reminderDao = reminderDao;
    }

    public PriorityBlockingQueue<ReminderEntity> getExpiringReminders() {
        return expiringReminders;
    }

    public void scheduledPollExpiringReminders() {
        while (true) {
            pollExpiringReminders();
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }


    public void eventTriggeredPollExpiringReminders() {

        pollExpiringReminders();
    }

    private void removeExistingReminders(List<ReminderEntity> reminders) {
        while(!expiringReminders.isEmpty()){
            expiringReminders.remove();
        }

    }


    private void pollExpiringReminders() {
        try {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            LocalTime inFiveMinutes = now.plusMinutes(5);
            List<ReminderEntity> reminders = reminderDao
                    .retrieveRemindersBetweenTimes(today, now, inFiveMinutes, false);
            removeExistingReminders(reminders);
            reminders.forEach(expiringReminders::offer);
            logger.info("Polling for expiring reminders");
            logger.info("The expiring reminders are:{}", expiringReminders);

        } catch (Exception e) {
            logger.error("Error occurred while polling expiring reminders: ", e);
        }
    }


    @Override
    public void run(String... args) throws Exception {
        new Thread(this::scheduledPollExpiringReminders).start();
    }
}
