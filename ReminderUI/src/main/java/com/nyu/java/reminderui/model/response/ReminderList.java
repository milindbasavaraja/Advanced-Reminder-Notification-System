package com.nyu.java.reminderui.model.response;

import java.util.List;

public class ReminderList {
    private List<Reminder> reminders;

    public ReminderList() {
    }

    public ReminderList(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }
}
