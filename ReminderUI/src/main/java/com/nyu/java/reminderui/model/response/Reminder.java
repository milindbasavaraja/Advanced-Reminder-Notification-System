package com.nyu.java.reminderui.model.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reminder {
    private Integer reminderId;
    private String reminderName;
    private String reminderWay;
    private LocalDate reminderDate;
    private LocalTime reminderTime;
    private String reminderDescription;
    private String reminderToEvent;
    private boolean expired;

    private String userName;

    public Reminder() {
    }

    public Reminder(Integer reminderId, String reminderName, String reminderWay, LocalDate reminderDate, LocalTime reminderTime, String reminderDescription, String reminderToEvent, boolean expired, String userName) {
        this.reminderId = reminderId;
        this.reminderName = reminderName;
        this.reminderWay = reminderWay;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderDescription = reminderDescription;
        this.reminderToEvent = reminderToEvent;
        this.expired = expired;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
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

    public String getReminderToEvent() {
        return reminderToEvent;
    }

    public void setReminderToEvent(String reminderToEvent) {
        this.reminderToEvent = reminderToEvent;
    }
}
