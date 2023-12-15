package com.nyu.java.reminderui.frames;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReminderCreationCallback {
    void createReminder(String name, String way, LocalDate date, LocalTime time, String description, String reminderToEvent);

    void updateReminder(Integer id, String name, String way, LocalDate date, LocalTime time, String description, String reminderToEvent);

}
