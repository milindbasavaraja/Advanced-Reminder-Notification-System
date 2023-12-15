package com.nyu.java.reminderui.frames;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.nyu.java.reminderui.model.response.Reminder;
import com.nyu.java.reminderui.model.response.UserInfoResponse;
import com.nyu.java.reminderui.utility.EmailValidator;
import com.nyu.java.reminderui.utility.PhoneNumberValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateReminderPopup extends JDialog {
    private Mode mode;
    private Integer reminderId;
    private JTextField reminderNameField;


    private JTextField reminderToEventField;
    private JLabel reminderToEventLabel;
    private JComboBox<String> reminderWayComboBox;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private JTextArea descriptionArea;
    private JButton submitButton;

    private ReminderCreationCallback callback;

    private UserInfoResponse userInfoResponse;

    public CreateReminderPopup(Frame parentFrame, ReminderCreationCallback callback, Mode mode, Integer reminderId,UserInfoResponse user) {
        super(parentFrame, "Create Reminder", true);
        this.mode = mode;
        this.reminderId = reminderId;
        this.userInfoResponse = user;
        initializeForm();
        pack();
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.callback = callback;
        System.out.println(user.getEmail());

    }

    private void initializeForm() {
        reminderNameField = new JTextField(20);
        reminderWayComboBox = new JComboBox<>(new String[]{"Email", "Message"});
        reminderToEventField = new JTextField(20);
        reminderToEventLabel = new JLabel("Email:");
        reminderToEventField.setText(userInfoResponse.getEmail());
        reminderToEventField.setEditable(false);
        reminderToEventField.setToolTipText("Enter email address");
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setInitialTimeToNow();
        timeSettings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.FiveMinutes, null, null);

        datePicker = new DatePicker();
        timePicker = new TimePicker(timeSettings);
        descriptionArea = new JTextArea(5, 20);
        submitButton = new JButton("Submit");

        descriptionArea = new JTextArea(5, 20);
        submitButton = new JButton("Submit");

        setLayout(new GridLayout(0, 1));
        add(new JLabel("Reminder Name:"));
        add(reminderNameField);
        add(new JLabel("Reminder Way:"));
        add(reminderWayComboBox);
        add(reminderToEventLabel);
        add(reminderToEventField);
        add(new JLabel("Date:"));
        add(datePicker);
        add(new JLabel("Time:"));
        add(timePicker);
        add(new JLabel("Description:"));
        add(new JScrollPane(descriptionArea));
        add(submitButton);


        reminderWayComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateReminderToEventField();
            }
        });

        submitButton.addActionListener(e -> {
            onSubmit();
        });
    }

    private void updateReminderToEventField() {
        String reminderWay = (String) reminderWayComboBox.getSelectedItem();
        if ("Email".equals(reminderWay)) {
            reminderToEventLabel.setText("Email:");
            reminderToEventField.setText(userInfoResponse.getEmail());
            reminderToEventField.setEditable(false);
        } else {
            reminderToEventField.setEditable(true);
            reminderToEventField.setText("");
            reminderToEventLabel.setText("Phone Number:");
            reminderToEventField.setToolTipText("Enter phone number in format +1XXXXXXXXXX");
        }
    }

    private void onSubmit() {
        String reminderName = reminderNameField.getText();
        String reminderWay = (String) reminderWayComboBox.getSelectedItem();
        LocalDate date = datePicker.getDate();
        LocalTime time = timePicker.getTime();
        String description = descriptionArea.getText();
        String reminderToEvent = "";


        reminderToEvent = validateInput(reminderName, reminderWay, date, time, description, reminderToEvent);
        if (reminderToEvent == null) return;
        dispose();

        if (mode == Mode.CREATE) {
            callback.createReminder(reminderName, reminderWay, date, time, description, reminderToEvent);
        } else {
            callback.updateReminder(reminderId, reminderName, reminderWay, date, time, description, reminderToEvent);
        }


    }

    private String validateInput(String reminderName, String reminderWay, LocalDate date, LocalTime time, String description, String reminderToEvent) {
        if (reminderName == null || reminderName.trim().isEmpty()) {
            dispose();
            errorPopup("Invalid Name");
            return null;
        }  else if (date == null || date.isBefore(LocalDate.now())) {
            dispose();
            errorPopup("Invalid Date");
            return null;
        } else if (time == null || (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
            dispose();
            errorPopup("Invalid Time");
            return null;
        } else if (description == null || description.trim().isEmpty()) {
            dispose();
            errorPopup("Invalid Description");
            return null;
        }
        else if ("Email".equalsIgnoreCase(reminderWay.trim())) {
            if (EmailValidator.isValidEmail(reminderToEventField.getText().trim())) {
                reminderToEvent = reminderToEventField.getText().trim();
            } else {
                dispose();
                errorPopup("Invalid Email");
                return null;
            }
        } else if ("Message".equalsIgnoreCase(reminderWay.trim())) {
            if (PhoneNumberValidator.isValidUSPhoneNumber(reminderToEventField.getText().trim())) {
                reminderToEvent = reminderToEventField.getText().trim();
            } else {
                dispose();
                errorPopup("Invalid Phone Number");
                return null;
            }
        }
        return reminderToEvent;
    }

    public static void showDialog(Frame parentFrame, ReminderCreationCallback callback, UserInfoResponse user) {

        CreateReminderPopup popup = new CreateReminderPopup(parentFrame, callback, Mode.CREATE, -1,user);
        popup.setVisible(true);

    }


    public void fillFormWithReminderData(Reminder reminder) {
        reminderNameField.setText(reminder.getReminderName());
        reminderWayComboBox.setSelectedItem(reminder.getReminderWay());
        datePicker.setDate(reminder.getReminderDate());
        timePicker.setTime(reminder.getReminderTime());
        descriptionArea.setText(reminder.getReminderDescription());
        reminderToEventField.setText(reminder.getReminderToEvent());
    }

    public void errorPopup(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
