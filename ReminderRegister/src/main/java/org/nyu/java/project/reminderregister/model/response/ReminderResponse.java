package org.nyu.java.project.reminderregister.model.response;

import org.nyu.java.project.reminderregister.entity.ReminderEntity;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReminderResponse {

    private Integer reminderId;


    private String reminderName;


    private String reminderWay;


    private LocalDate reminderDate;


    private LocalTime reminderTime;


    private String reminderDescription;


    private Boolean isExpired;


    private String reminderToEvent;

    public ReminderResponse(Integer reminderId, String reminderName, String reminderWay, LocalDate reminderDate, LocalTime reminderTime, String reminderDescription, Boolean isExpired, String reminderToEvent) {
        this.reminderId = reminderId;
        this.reminderName = reminderName.trim();
        this.reminderWay = reminderWay.trim();
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderDescription = reminderDescription.trim();
        this.isExpired = isExpired;
        this.reminderToEvent = reminderToEvent.trim();
    }

    public ReminderResponse(ReminderEntity reminderEntity){
        this(reminderEntity.getReminderId(),reminderEntity.getReminderName(),reminderEntity.getReminderWay(),reminderEntity.getReminderDate(),reminderEntity.getReminderTime(),reminderEntity.getReminderDescription(),reminderEntity.getExpired(),reminderEntity.getReminderToEvent());
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

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public String getReminderToEvent() {
        return reminderToEvent;
    }

    public void setReminderToEvent(String reminderToEvent) {
        this.reminderToEvent = reminderToEvent;
    }
}
