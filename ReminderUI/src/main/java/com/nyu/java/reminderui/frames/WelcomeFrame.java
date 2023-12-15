package com.nyu.java.reminderui.frames;

import com.nyu.java.reminderui.client.ReminderClient;
import com.nyu.java.reminderui.config.TokenStorage;
import com.nyu.java.reminderui.exceptions.ReminderCreationException;
import com.nyu.java.reminderui.exceptions.ReminderUpdateException;
import com.nyu.java.reminderui.model.request.ReminderCreationRequest;
import com.nyu.java.reminderui.model.response.Reminder;
import com.nyu.java.reminderui.model.response.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class WelcomeFrame extends JPanel implements ReminderCreationCallback {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeFrame.class);

    private final JButton createReminderButton = new JButton("Create A Reminder");
    private final JButton viewAllRemindersButton = new JButton("View All Reminders");
    private final JButton logOutButton = new JButton("Log Out");
    private final JLabel displayTextLabel = new JLabel(" ", SwingConstants.CENTER);

    private final ReminderClient reminderClient;
    private final ApplicationEventPublisher publisher;

    private static UserInfoResponse user;


    @Autowired
    public WelcomeFrame(ApplicationEventPublisher publisher, ReminderClient reminderClient) {

        user =new UserInfoResponse();
        this.publisher = publisher;
        this.reminderClient = reminderClient;
        refreshUserName();
        initializeComponents();
    }

    public void refreshFrame() {

        refreshUserName();
    }

    public void refreshFrame(UserInfoResponse userInfoResponse) {
        user = userInfoResponse;
        refreshUserName();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        add(createButtonPanel(), BorderLayout.PAGE_END);
        add(displayTextLabel, BorderLayout.PAGE_START);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(createReminderButton);
        buttonPanel.add(viewAllRemindersButton);
        buttonPanel.add(logOutButton);
        logOutButton.addActionListener(e -> handleLogout());
        viewAllRemindersButton.addActionListener(e -> {
            publisher.publishEvent(new SwitchFrameEvent(this, "Reminder"));
        });

        createReminderButton.addActionListener(e -> {
            CreateReminderPopup.showDialog((Frame) SwingUtilities.getWindowAncestor(this), this,user);
            logger.info("Returning");
        });
        return buttonPanel;
    }

    private void refreshUserName() {

        try {
            user = reminderClient.retrieveUserNameFromJwtToken(TokenStorage.getToken());
            displayTextLabel.setText("Welcome " + user.getUsername());

        } catch (Exception e) {
            displayError("User not found");
        }

    }

    private void handleLogout() {
        TokenStorage.clearToken();
        displayTextLabel.setText(" ");
        publisher.publishEvent(new SwitchFrameEvent(this, "Login"));
    }

    private void displayError(String message) {
        logger.error("No user found: {}", message);

    }

    @Override
    public void createReminder(String reminderName, String reminderWay, LocalDate reminderDate, LocalTime reminderTime, String reminderDescription, String reminderToEvent) {
        try {
            refreshUserName();
            ReminderCreationRequest reminderCreationRequest = new ReminderCreationRequest(reminderName, reminderWay, reminderDate, reminderTime, reminderDescription, user.getUsername(), reminderToEvent);
            reminderClient.createNewReminder(reminderCreationRequest);
            JOptionPane.showMessageDialog(null, "Reminder created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ReminderCreationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateReminder(Integer id, String name, String way, LocalDate date, LocalTime time, String description, String reminderToEvent) {
        try {
            refreshUserName();
            Reminder reminder = new Reminder(id, name, way, date, time, description, reminderToEvent, false, user.getUsername());
            reminderClient.updateReminder(reminder);
            JOptionPane.showMessageDialog(null, "Reminder updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            publisher.publishEvent(new SwitchFrameEvent(this, "Reminder"));
        } catch (ReminderUpdateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static UserInfoResponse getUser() {
        return user;
    }


}
