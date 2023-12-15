package com.nyu.java.reminderui.model.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReminderCreationRequest {
    private String reminderName;
    private String reminderWay;
    private LocalDate reminderDate;
    private LocalTime reminderTime;
    private String reminderDescription;

    private String reminderToEvent;
    private String userName;

    public ReminderCreationRequest(String reminderName, String reminderWay, LocalDate reminderDate, LocalTime reminderTime, String reminderDescription, String userName, String reminderToEvent) {
        this.reminderName = reminderName;
        this.reminderWay = reminderWay;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderDescription = reminderDescription;
        this.userName = userName;
        this.reminderToEvent = reminderToEvent;
    }

    public String getReminderToEvent() {
        return reminderToEvent;
    }

    public void setReminderToEvent(String reminderToEvent) {
        this.reminderToEvent = reminderToEvent;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getReminderWay() {
        return reminderWay;
    }

    public void setReminderWay(String reminderWay) {
        this.reminderWay = reminderWay;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
