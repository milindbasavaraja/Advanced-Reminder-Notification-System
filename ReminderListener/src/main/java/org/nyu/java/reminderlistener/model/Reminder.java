package org.nyu.java.reminderlistener.model;

public class Reminder {

    private String reminderName;
    private String reminderDescription;
    private String reminderDate;
    private String reminderTime;

    private String reminderWay;
    private String reminderToEvent;

    public Reminder() {
    }

    public Reminder(String reminderName, String reminderDescription, String reminderDate, String reminderTime, String reminderToEvent,String reminderWay) {
        this.reminderName = reminderName;
        this.reminderDescription = reminderDescription;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderToEvent = reminderToEvent;
        this.reminderWay =  reminderWay;
    }

    public String getReminderWay() {
        return reminderWay;
    }

    public void setReminderWay(String reminderWay) {
        this.reminderWay = reminderWay;
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

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}
