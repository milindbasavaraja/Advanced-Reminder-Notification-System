package org.nyu.java.project.reminderregister.dao;

import org.nyu.java.project.reminderregister.entity.ReminderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReminderDao {

    private final DataSource dataSource;


    private Logger logger = LoggerFactory.getLogger(ReminderDao.class);

    public ReminderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void insertReminder(ReminderEntity reminder) throws SQLException {
        String sql = "INSERT INTO reminders (reminderName, reminderWay, reminderDate, reminderTime, reminderDescription, isExpired, reminderToEvent, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reminder.getReminderName());
            pstmt.setString(2, reminder.getReminderWay());
            pstmt.setDate(3, Date.valueOf(reminder.getReminderDate()));
            pstmt.setTime(4, Time.valueOf(reminder.getReminderTime()));
            pstmt.setString(5, reminder.getReminderDescription());
            pstmt.setBoolean(6, reminder.getExpired());
            pstmt.setString(7, reminder.getReminderToEvent());
            pstmt.setLong(8, reminder.getUserId());
            pstmt.executeUpdate();
        }
    }


    public void updateReminder(ReminderEntity reminder) throws SQLException {
        String sql = "UPDATE reminders SET reminderName = ?, reminderWay = ?, reminderDate = ?, reminderTime = ?, reminderDescription = ?, isExpired = ?, reminderToEvent = ?, user_id = ? WHERE reminderId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reminder.getReminderName());
            pstmt.setString(2, reminder.getReminderWay());
            pstmt.setDate(3, Date.valueOf(reminder.getReminderDate()));
            pstmt.setTime(4, Time.valueOf(reminder.getReminderTime()));
            pstmt.setString(5, reminder.getReminderDescription());
            pstmt.setBoolean(6, reminder.getExpired());
            pstmt.setString(7, reminder.getReminderToEvent());
            pstmt.setLong(8, reminder.getUserId());
            pstmt.setInt(9, reminder.getReminderId());
            pstmt.executeUpdate();
        }
    }

    public void deleteReminder(Integer reminderId) throws SQLException {
        String sql = "DELETE FROM reminders WHERE reminderId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reminderId);
            pstmt.executeUpdate();
        }
    }


    public List<ReminderEntity> retrieveRemindersBetweenTimes(LocalDate todayDate, LocalTime startTime, LocalTime endTime,boolean isExpired) throws SQLException {
        String sql = "SELECT * FROM reminders WHERE reminderTime BETWEEN ? AND ? AND reminderDate = ? AND isExpired = ?";
        List<ReminderEntity> reminders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTime(1, Time.valueOf(startTime));
            pstmt.setTime(2, Time.valueOf(endTime));
            pstmt.setDate(3, Date.valueOf(todayDate));
            pstmt.setBoolean(4, isExpired);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reminders.add(mapToReminderEntity(rs));
                }
            }
        }
        return reminders;
    }

    public List<ReminderEntity> findRemindersByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM reminders WHERE user_id = ?";
        List<ReminderEntity> reminders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reminders.add(mapToReminderEntity(rs));
                }
            }
        }
        return reminders;
    }

    public Optional<ReminderEntity> findReminderById(Integer reminderId) throws SQLException {
        String sql = "SELECT * FROM reminders WHERE reminderId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reminderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToReminderEntity(rs));
                }
            }
        }
        return Optional.empty();
    }


    private ReminderEntity mapToReminderEntity(ResultSet rs) throws SQLException {
        ReminderEntity reminder = new ReminderEntity();
        reminder.setReminderId(rs.getInt("reminderId"));
        reminder.setReminderName(rs.getString("reminderName"));
        reminder.setReminderWay(rs.getString("reminderWay"));
        reminder.setReminderDate(rs.getDate("reminderDate").toLocalDate());
        reminder.setReminderTime(rs.getTime("reminderTime").toLocalTime());
        reminder.setReminderDescription(rs.getString("reminderDescription"));
        reminder.setExpired(rs.getBoolean("isExpired"));
        reminder.setReminderToEvent(rs.getString("reminderToEvent"));
        reminder.setUserId(rs.getLong("user_id"));

        return reminder;
    }

}
