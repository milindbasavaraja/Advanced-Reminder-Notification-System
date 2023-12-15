package com.nyu.java.reminderui.frames;

import com.nyu.java.reminderui.client.ReminderClient;
import com.nyu.java.reminderui.exceptions.NoRemindersFoundException;
import com.nyu.java.reminderui.exceptions.ReminderDeleteException;
import com.nyu.java.reminderui.model.response.Reminder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;


@Component
public class ReminderPanels extends JPanel {

    private Logger logger = LoggerFactory.getLogger(ReminderPanels.class);

    private ReminderClient reminderClient;
    private ReminderCreationCallback callback;

    private ApplicationEventPublisher publisher;

    public ReminderPanels(ReminderClient reminderClient, ReminderCreationCallback callback, ApplicationEventPublisher publisher) {

        this.reminderClient = reminderClient;
        this.callback = callback;
        this.publisher = publisher;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        logger.info("The user is:{}", WelcomeFrame.getUser());
        if (WelcomeFrame.getUser() != null && WelcomeFrame.getUser().getUsername() == null) {
            refreshReminders();
        }

    }


    public void refreshReminders() {
        logger.info("Bye");
        if (WelcomeFrame.getUser() == null || WelcomeFrame.getUser().getUsername()==null) {

            return;
        }
        removeAll();
        try {
            List<Reminder> reminders = reminderClient.getRemindersForUser();
            for (Reminder reminder : reminders) {
                add(createReminderPanel(reminder));
            }
            add(createBackButtonPanel());
            revalidate();
            repaint();
        } catch (NoRemindersFoundException e) {
            JOptionPane.showMessageDialog(ReminderPanels.this,
                    e.getMessage(),
                    "Reminder Retrieval Error",
                    JOptionPane.ERROR_MESSAGE);
        }


    }

    private JPanel createReminderPanel(Reminder reminder) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel(reminder.getReminderName()));


        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> onUpdate(reminder));
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> onDelete(reminder));
        panel.add(deleteButton);

        return panel;
    }

    private JPanel createBackButtonPanel() {
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            logger.info("Back Button clicked");
            publisher.publishEvent(new SwitchFrameEvent(this, "Welcome"));
        });
        backButtonPanel.add(backButton);
        return backButtonPanel;
    }

    private void onUpdate(Reminder reminder) {
        CreateReminderPopup popup = new CreateReminderPopup(
                null,
                callback,
                Mode.UPDATE,
                reminder.getReminderId(),
                WelcomeFrame.getUser()
        );
        popup.fillFormWithReminderData(reminder);
        popup.setVisible(true);

    }

    private void onDelete(Reminder reminder) {
        try {
            logger.info("Deleting item with id:{}", reminder.getReminderId());
            reminderClient.deleteReminder(reminder.getReminderId());
            refreshReminders();
        } catch (ReminderDeleteException e) {

        }

    }


}
