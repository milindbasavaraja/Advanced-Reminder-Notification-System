package org.nyu.java.project.reminderregister.config;

import org.nyu.java.project.reminderregister.exception.TableCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initialize() throws TableCreationException {
        createTablesIfNotExists();

    }

    private void createTablesIfNotExists() throws TableCreationException {
        //jdbcTemplate.execute("USE REMINDER_DB");

        String userTableCreationSqlCommand = "CREATE TABLE IF NOT EXISTS users (" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "    username VARCHAR(50) NOT NULL," +
                "    email VARCHAR(100) NOT NULL," +
                "    password VARCHAR(100)," +
                "    otp VARCHAR(50)," +
                "    otpRequestedTime BIGINT," +
                "    isEmailVerified BOOLEAN," +
                "    role VARCHAR(50)," +
                "    UNIQUE (username)," +
                "    UNIQUE (email)" +
                ");";

        executeSQLCommand(userTableCreationSqlCommand, "Users");

        /*String roleTableCreationSqlCommand = "CREATE TABLE IF NOT EXISTS roles (" +
                "    id INT AUTO_INCREMENT PRIMARY KEY," +
                "    name VARCHAR(20)" +
                ");";

        executeSQLCommand(roleTableCreationSqlCommand, "Role");*/

        String reminderTableCreationSqlCommand = "CREATE TABLE IF NOT EXISTS reminders (" +
                "    reminderId INT AUTO_INCREMENT PRIMARY KEY," +
                "    reminderName VARCHAR(255) NOT NULL," +
                "    reminderWay VARCHAR(255) NOT NULL," +
                "    reminderDate DATE NOT NULL," +
                "    reminderTime TIME NOT NULL," +
                "    reminderDescription VARCHAR(500) NOT NULL," +
                "    isExpired BOOLEAN," +
                "    reminderToEvent VARCHAR(255) NOT NULL," +
                "    user_id BIGINT," +
                "    FOREIGN KEY (user_id) REFERENCES users(id)" +
                ");";

        executeSQLCommand(reminderTableCreationSqlCommand, "Reminder");


    }

    private void executeSQLCommand(String command, String tableName) throws TableCreationException {
        try {
            jdbcTemplate.execute(command);
        } catch (Exception e) {
            String msg = String.format("Error while creating table: %s, error: %s", tableName, e.getMessage());
            throw new TableCreationException(msg);
        }
    }
}
