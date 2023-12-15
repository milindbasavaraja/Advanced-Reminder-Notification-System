package org.nyu.java.project.reminderregister.entity;


import java.time.LocalDate;
import java.time.LocalTime;


public class ReminderEntity {

    private Integer reminderId;


    private String reminderName;


    private String reminderWay;


    private LocalDate reminderDate;


    private LocalTime reminderTime;


    private String reminderDescription;


    private Boolean isExpired;


    private String reminderToEvent;


    private Long userId;

    public ReminderEntity() {
    }

    public ReminderEntity(String reminderName, String reminderWay, LocalDate reminderDate, LocalTime reminderTime, String reminderDescription,String reminderToEvent) {
        this.reminderName = reminderName;
        this.reminderWay = reminderWay;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderDescription = reminderDescription;
        this.isExpired = false;
        this.reminderToEvent  = reminderToEvent;
    }

    public String getReminderToEvent() {
        return reminderToEvent;
    }

    public void setReminderToEvent(String reminderToEvent) {
        this.reminderToEvent = reminderToEvent;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer id) {
        this.reminderId = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    @Override
    public String toString() {
        return "ReminderEntity{" +
                "reminderName='" + reminderName + '\'' +
                ", reminderWay='" + reminderWay + '\'' +
                ", reminderDate=" + reminderDate +
                ", reminderTime=" + reminderTime +
                ", reminderDescription='" + reminderDescription + '\'' +
                ", isExpired=" + isExpired +
                ", user=" + userId +
                '}';
    }
}
